package org.x.job.dynamiccompile;

import org.shoper.log.util.LogFactory;
import org.shoper.log.util.Logger;
import sun.misc.ClassLoaderUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * class loader handler. 提供对 jar java file java code 动态加载提供
 *
 * @author CathyZhuzhu
 */
public class ClassLoaderHandler {
    private static Logger logger = LogFactory
            .getLogger(ClassLoaderHandler.class);
    private VolatileObject<URLClassLoader> classLoader;
    private ClassInfo classInfo;
    private Manifest manifest;
    private JarFile jarFile;
    private static final String USER_DIR = System.getProperty("java.io.tmpdir");
    private static final String SUB_DIR = "/classpath";
    private static final String CLASSDIR = USER_DIR + SUB_DIR;
    private String TOKEN = MD5Util.GetMD5Code(System.currentTimeMillis() + "");
    private String TARGETCLASSDIR = CLASSDIR + "/" + TOKEN;
    private static final String MANIFEST = "META-INF/MANIFEST.MF";
    private CompileType compileType;
    private static final String FileSeparator = File.separator;

    enum CompileType {
        FILE, JAR, CODE
    }

    private static boolean isUnix = true;

    /**
     * 检查系统环境 <br>
     * Created by ShawnShoper 2016年6月2日
     */
    void checkSystem() {
        logger.info("Checking OS.....");
        String osName = System.getProperties().getProperty("os.name");
        if (osName.contains("Windows")) {
            logger.info("Current OS is windows...");
            isUnix = false;
        }
        logger.info("Checking compile output directory has exists....");
        File file = new File(CLASSDIR);
        if (!file.exists()) {
            logger.info("Creating compile output directory ....");
            if (file.mkdirs()) {
                logger.info("Compile output directory created");
            } else {
                logger.info(
                        "Create compile output directory fail,check the curr user has permission to write or right...compile output is {}",
                        CLASSDIR
                );
                throw new ExceptionInInitializerError(
                        "Create compile output directory fail,check the curr user has permission to write or right...compile output is "
                                + CLASSDIR);
            }
            ;
        }
    }

    private ClassLoaderHandler() {

    }

    public static ClassLoaderHandler newInstance(URL url) {
        return newInstance(new URL[]{url});
    }

    public static ClassLoaderHandler newInstance() {
        return newInstance(new URL[]{});
    }

    public static ClassLoaderHandler newInstance(URL[] url) {

        ClassLoaderHandler clh = new ClassLoaderHandler();
        clh.checkSystem();
        URLClassLoader classLoader = null;
        classLoader = clh.loadClassLoader(url);
        clh.classLoader = new VolatileObject<URLClassLoader>(classLoader);
        return clh;
    }

    /**
     * 获取 classLoader 操作实例 <br>
     * Created by ShawnShoper 2016年6月2日
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static ClassLoaderHandler newInstanceFromJar(File file)
            throws IOException {
        ClassLoaderHandler clh = newInstance(file.toURI().toURL());
        clh.compileType = CompileType.JAR;
        clh.readJarInfo(file);
        return clh;
    }

    /**
     * 从 manifest 中加载 Main-class <br>
     * Created by ShawnShoper 2016年6月2日
     *
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> getClassFromManifest() throws ClassNotFoundException {
        return getClassByName(
                manifest.getMainAttributes().getValue(Name.MAIN_CLASS));
    }

    public Class<?> getClassByName(String className)
            throws ClassNotFoundException {
        if (StringUtil.isEmpty(className))
            throw new RuntimeException("Class name can not be null");
        Class<?> classz = classLoader.getObject().loadClass(className);
        return classz;
    }

    public VolatileObject<URLClassLoader> getClassLoader() {
        return classLoader;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public JarFile getJarFile() {
        return jarFile;
    }

    /**
     * 读取 jarFile <br>
     * Created by ShawnShoper 2016年6月2日
     *
     * @param file
     * @throws IOException
     */
    public void readJarInfo(File file) throws IOException {
        try {
            jarFile = new JarFile(file);
            readManifest(jarFile);
        } finally {
            if (Objects.nonNull(jarFile))
                jarFile.close();
        }
    }

    public void readManifest(JarFile jarFile) throws IOException {
        InputStream inputStream = null;
        ZipEntry zipEntry = jarFile.getEntry(MANIFEST);
        try {
            inputStream = jarFile.getInputStream(zipEntry);
            manifest = new Manifest(inputStream);
        } finally {
            if (Objects.nonNull(inputStream))
                inputStream.close();
        }
    }

    public ClassInfo getClassInfo() {
        return classInfo;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    private URLClassLoader loadClassPath(URL[] urls) {
        URLClassLoader loader;
        if (urls.length == 0)
            loader = URLClassLoader.newInstance(buildClassPathURLs(), this.getClass().getClassLoader());
        else {
            List<URL> rootUrl = new ArrayList<>();
            for (URL url : buildClassPathURLs()) {
                rootUrl.add(url);
            }
            if (urls != null && urls.length != 0)
                for (URL url : urls) {
                    rootUrl.add(url);
                }
            URL[] url = rootUrl.toArray(new URL[]{});
            loader = URLClassLoader.newInstance(url, this.getClass().getClassLoader());
        }
        return loader;
    }

    private URLClassLoader loadClassLoader(URL[] urls) {
        return loadClassPath(urls);
    }

    public Class<?> getClassFromJavaFile(final File file)
            throws ClassNotFoundException, IOException {
        compileType = CompileType.FILE;
        if (Objects.isNull(file))
            throw new NullPointerException();
        if (!file.exists())
            throw new RuntimeException(
                    String.format("文件%s不存在", file.getAbsolutePath()));
        // 移动文件到temp 下
        File destFile = new File(TARGETCLASSDIR + "/" + file.getName());
        FileUtils.copyFile(file, destFile);
        // 编译文件...
        if (!compile(destFile))
            throw new RuntimeException(
                    String.format("编译文件%s失败", file.getAbsolutePath()));
        // read java file content
        if (!CompileType.CODE.equals(compileType))
            classInfo = parseClassInfo(destFile);
        // 编译完成后删除源文件..
        deleteFile(destFile);
        loadInnerClass(classInfo);
        return getClassLoader().getObject().loadClass(classInfo.getFullName());
    }

    private void deleteFile(File destFile) throws IOException {
        FileUtils.forceDelete(destFile);
    }

    /**
     * 关闭 class loader <br>
     * Created by ShawnShoper 2016年6月2日
     */
    public void close() throws IOException {
        if (CompileType.FILE.equals(compileType))
            // 如果是绑定的文件,那么在关闭 classloader 的时候删除相关的 class 文件
            cleanFile();
        ClassLoaderUtil.releaseLoader(classLoader.getObject());
    }

    private void cleanFile() throws IOException {
        File file = new File(TARGETCLASSDIR);
        FileUtils.forceDelete(file);
    }

    private ClassInfo parseClassInfo(File destFile) throws FileNotFoundException {
        StringBuilder content = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader br = null;
        try {
            inputStream = new FileInputStream(destFile);
            br = new BufferedReader(new InputStreamReader(
                    inputStream,
                    Charset.forName("UTF-8")
            ));
            br.lines().forEach(line ->
                                       content.append(line + "\n")
            );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (!Objects.nonNull(inputStream)) {
                    inputStream.close();
                }
                if (Objects.nonNull(br)) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return parseClassInfo(content.toString());
    }
    /**
     * 通过 class 文件获取类名<br>
     * Created by ShawnShoper 2016年5月31日
     *
     * @param is
     * @return
     * @throws IOException
     */
    // public String getClassName(InputStream is) throws IOException
    // {
    // // 这部分属于 jdk 1.8,记得不要往下更换 JDK
    // ClassReader cr = new ClassReader(is);
    // String className = cr.getClassName();
    // return className.substring(className.lastIndexOf("/") + 1);
    // }

    /**
     * 加载相关内部类...
     *
     * @param ci
     * @throws ClassNotFoundException
     */
    private void loadInnerClass(final ClassInfo ci)
            throws ClassNotFoundException {
        String classPath = TARGETCLASSDIR + FileSeparator
                + ci.getPackageName().replace(".", "/") + "/"
                + ci.getClassName();
        File file = new File(classPath);
        //判断当前加载的 class 是否有内部类
        File[] files = file.getParentFile().listFiles((dir, name) ->
            name.startsWith(ci.getClassName() + "$")
        );
        //把生成的 class 以及 class 相关的内部类 load 到JVM里
        for (File ignored : files) {
            getClassLoader().getObject().loadClass(ci.getFullName());
        }
    }

    @Deprecated
    private URLClassLoader loadClassPath(URL url) {
        return loadClassPath(new URL[]{url});
    }

    URL[] buildClassPathURLs() {
        List<URL> urls = new ArrayList<URL>();
        // 构造file URL
        File file = new File(TARGETCLASSDIR);
        urls.addAll(getFileURLs(file));
        return urls.toArray(new URL[]{});
    }

    /**
     * 获取需要动态加载的区域..扫描整个编译的临时目录...
     *
     * @param file
     * @return
     */
    static List<URL> getFileURLs(File file) {
        if (file == null)
            throw new NullPointerException("The file must be not null..");
        if (!file.exists())
            file.mkdir();
        List<URL> urls = new ArrayList<URL>();
        // 检查是否是隐藏文件,不扫描隐藏文件夹...
        {
            URL url = buildURL(file);
            if (url != null) {
                urls.add(url);
            }
        }
        try {
            for (File f : file.listFiles(pathname -> pathname.getName().startsWith(".") ? false : true)) {
                if (f.isDirectory()) {
                    URL url = buildURL(f);
                    if (url != null) {
                        urls.addAll(getFileURLs(f));
                    }
                }
            }
        }catch (NullPointerException e){
            throw new IllegalAccessError(CLASSDIR+" Permission denied");
        }
        return urls;
    }

    /**
     * File 转 URL
     *
     * @param file
     * @return
     */
    static URL buildURL(File file) {
        URL url = null;
        try {
            url = new URL("file:" + (!isUnix ? "/" : "")
                                  + file.getAbsolutePath() + FileSeparator);
        } catch (MalformedURLException e) {
            logger.error("url format error , message is {}", e.getMessage());
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Write java string to file , Preparing to compile
     *
     * @param java
     * @return
     * @throws IOException
     */
    private File generateFile(String fileName, String java) throws IOException {
        logger.info("Step 2:Write java string to file...");
        String floder = TARGETCLASSDIR + FileSeparator + "tmp";
        File destFile = new File(floder);
        FileUtils.forceMkdir(destFile);
        File file = new File(floder + FileSeparator + fileName + ".java");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(java.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error(
                    "Write java strng to file fail.....message is {}",
                    e.getMessage()
            );
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Parse string to get className
     *
     * @param java
     * @return
     */
    private String parseClassName(String java) {
        logger.info("Step 1:Parse java string to get className...");
        int prefix_index = java.indexOf(" class ") + 7;
        int stufix_index1 = java.substring(prefix_index).indexOf(" ");
        int stufix_index2 = java.substring(prefix_index).indexOf("{");
        int stufix_index = stufix_index2 > stufix_index1
                ? stufix_index1
                : stufix_index2;
        String className = java.substring(
                prefix_index,
                prefix_index + stufix_index
        );
        className = className.replaceAll("\\s*", "");
        return className;
    }

    /**
     * parse package name
     **/
    private String parsePackageName(String java) {
        java = java.replaceAll("(?>/\\*)[^(\\*/)]*\\*/", "");
        if (!java.startsWith("package")) return "";
        String packageName = java.substring(
                java.indexOf("package") + 8,
                java.indexOf(";")
        );
        return packageName;
    }

    public ClassInfo parseClassInfo(String java) {
        String cn = parseClassName(java);
        String pn = parsePackageName(java);

        ClassInfo ci = new ClassInfo(cn, pn);
        return ci;
    }

    /**
     * Compile java file by java file String.
     *
     * @param file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean compile(File file) {
        logger.info("Preparing to compile java file.....");
        File floder = new File(TARGETCLASSDIR);
        if (!floder.exists())
            floder.mkdirs();
        if (logger.isDebugEnabled())
            logger.debug(TARGETCLASSDIR);
        int result = Main.compile(new String[]{"-d", TARGETCLASSDIR,
                TARGETCLASSDIR + FileSeparator
                        + file.getName()});
        return result == 0 ? true : false;
    }

    /**
     * 通过java文件获取class...
     *
     * @param code
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Class<?> getClassFromJavaCode(final String code)
            throws IOException, ClassNotFoundException {
        return getClass1(code);
    }

    /**
     * 通过 class 信息,以及源代码进行编译加载代码.</br>
     * 返回 class </br>
     * Created by ShawnShoper 2016年5月31日
     *
     * @param code
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private Class<?> getClass1(String code)
            throws IOException, ClassNotFoundException {
        compileType = CompileType.CODE;
        ClassInfo ci = parseClassInfo(code);
        File file = generateFile(ci.getClassName(), code);
        return getClassFromJavaFile(file);
    }

}
