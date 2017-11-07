package org.shoper.dynamiccompile;

import com.sun.tools.javac.Main;
import org.shoper.commons.MD5Util;
import org.shoper.commons.StringUtil;
import org.shoper.dynamiccompile.info.ClassInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * Dynamic Compile and loader java.... Call method 动态编译加载
 *
 * @author CathyZhuZhu
 */
public class JDKCompile
{
    /**
     * class缓存验证类
     */
    private static ClassVerify cv = new ClassVerify();
    private static Logger logger = LoggerFactory.getLogger(JDKCompile.class);
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String SUB_DIR = "/tmp";
    private static final String CLASSDIR = USER_DIR + SUB_DIR;
    private static boolean isUnix = true;
    static URLClassLoader loader;
    // URLClassLoader.newInstance(buildClassPathURLs());
    // checking compile output directory....
    static
    {
        logger.info("Checking OS.....");
        String osName = System.getProperties().getProperty("os.name");
        if (osName.contains("Windows"))
        {
            logger.info("Current OS is windows...");
            isUnix = false;
        }
        logger.info("Checking compile output directory has exists....");
        File file = new File(CLASSDIR);
        if (!file.exists())
        {
            logger.info("Creating compile output directory ....");
            if (file.mkdirs())
            {
                logger.info("Compile output directory created");
            } else
            {
                logger.info(
                        "Create compile output directory fail,check the curr user has permission to write or right...compile output is {}",
                        CLASSDIR);
                throw new ExceptionInInitializerError(
                        "Create compile output directory fail,check the curr user has permission to write or right...compile output is "
                                + CLASSDIR);
            } ;
        }
        // loader = loadClassPath();
    }

    /**
     * Compile java file by java file String.
     *
     * @param file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static boolean compile(File file)
    {
        logger.info("Preparing to compile java file.....");
        int result = Main.compile(new String[]{"-d", CLASSDIR,
                CLASSDIR.substring(USER_DIR.length() + 1) + File.separator
                        + file.getName()});
        return result == 0 ? true : false;
    }
    /**
     * 检查当前内是否被缓存<br>
     * Created by ShawnShoper 2016年5月31日
     *
     * @param key
     * @param value
     * @return
     */
    private static boolean verify(String key, String value)
    {
        Object old = cv.getValue(key);
        if (Objects.isNull(old) || !old.equals(value))
            return false;
        else
            return true;
    }
    /**
     * 通过java文件获取class...
     *
     * @param code
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> getClass(final String code)
            throws ClassNotFoundException
    {
        loader = loadClassPath(buildClassPathURLs());
        ClassInfo ci = parseClassInfo(code);
        Class<?> clazz = null;
        String newz = MD5Util.GetMD5Code(code);
        String className = ci.getPackageName() + "." + ci.getClassName();
        if (verify(className, newz))
            clazz = loader.loadClass(className);
        else
        {
            clazz = getClass1(ci, code);
            cv.putClazzVerify(className, newz);
        }
        return clazz;
    }
    public static Class<?> getClass(File file)
            throws ClassNotFoundException, IOException
    {
        JarFile jarFile = null;
        String mainClass = null;
        URLClassLoader classLoader = null;
        InputStream inputStream = null;
        try
        {
            jarFile = new JarFile(file);
            ZipEntry zipEntry = jarFile.getEntry("META-INF/MANIFEST.MF");
            inputStream = jarFile.getInputStream(zipEntry);
            Manifest manifest = new Manifest(inputStream);
            mainClass = manifest.getMainAttributes()
                    .getValue(Attributes.Name.MAIN_CLASS);
            if (StringUtil.isEmpty(mainClass))
            {
                throw new IllegalAccessError(
                        "The jar file has not container manifest.mf");
            }
            classLoader = loadClassPath(file.toURI().toURL());
        } finally
        {
            if (Objects.nonNull(inputStream))
                inputStream.close();
            if (Objects.nonNull(jarFile))
                jarFile.close();
        }
        return classLoader.loadClass(mainClass);
    }
    public static void main(String[] args) throws Exception
    {
        Class<?> class1 = JDKCompile
                .getClass(new File("/Users/ShawnShoper/Desktop/a.jar"));
        Object object = class1.newInstance();
        class1.getMethod("A", null).invoke(object, null);
    }
    /**
     * 通过 class 文件获取类名<br>
     * Created by ShawnShoper 2016年5月31日
     *
     * @param is
     * @return
     * @throws IOException
     */
    // public static String getClassName(InputStream is) throws IOException
    // {
    // // 这部分属于 jdk 1.8,记得不要往下更换 JDK
    // ClassReader cr = new ClassReader(is);
    // String className = cr.getClassName();
    // return className.substring(className.lastIndexOf("/") + 1);
    // }
    /**
     * 通过 class 信息,以及源代码进行编译加载代码.</br>
     * 返回 class </br>
     * Created by ShawnShoper 2016年5月31日
     *
     * @param ci
     * @param main
     * @return
     * @throws ClassNotFoundException
     */
    private static Class<?> getClass1(ClassInfo ci, String main)
            throws ClassNotFoundException
    {
        Class<?> clazz = null;
        File file = generateFile(ci.getClassName(), main);
        if (compile(file))
        {

            loader = loadClassPath(buildClassPathURLs());
            loadInnerClass(ci);
            clazz = loader
                    .loadClass(ci.getPackageName() + "." + ci.getClassName());
        } else
            throw new RuntimeException(
                    "Compliing JAVA file failed...please check the java file content or user has permission to access or file is exists...the path is "
                            + CLASSDIR);
        return clazz;
    }

    /**
     * 加载相关内部类...
     *
     * @param ci
     * @throws ClassNotFoundException
     */
    private static void loadInnerClass(final ClassInfo ci)
            throws ClassNotFoundException
    {
        String classPath = USER_DIR + File.separator + SUB_DIR + File.separator
                + ci.getPackageName().replace(".", "/") + "/"
                + ci.getClassName();
        File file = new File(classPath);
        File[] files = file.getParentFile().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.startsWith(ci.getClassName() + "$");
            }
        });
        for (File f : files)
        {
            loader.loadClass(ci.getPackageName() + "."
                    + f.getName().substring(0, f.getName().indexOf(".")));
        }
    }

    /**
     * 重新加载classPath
     *
     * @return
     */
    private static URLClassLoader loadClassPath(URL[] urls)
    {
        loader = URLClassLoader.newInstance(urls,JDKCompile.class.getClassLoader());
        return loader;
    }
    private static URLClassLoader loadClassPath(URL url)
    {
        return loadClassPath(new URL[]{url});
    }
    public static URL[] buildClassPathURLs()
    {
        List<URL> urls = new ArrayList<URL>();
        // 构造file URL
        File file = new File(USER_DIR + SUB_DIR);
        urls.addAll(getFileURLs(file));
        return urls.toArray(new URL[]{});
    }

    /**
     * 获取需要动态加载的区域..扫描整个编译的临时目录...
     *
     * @param file
     * @return
     */
    private static List<URL> getFileURLs(File file)
    {
        if (file == null)
            throw new NullPointerException("The file must be not null..");
        List<URL> urls = new ArrayList<URL>();
        // 检查是否是隐藏文件,不扫描隐藏文件夹...
        {
            URL url = buildURL(file);
            if (url != null)
            {
                urls.add(url);
            }
        }
        for (File f : file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.getName().startsWith(".") ? false : true;
            }
        }))
        {
            if (f.isDirectory())
            {
                URL url = buildURL(f);
                if (url != null)
                {
                    urls.addAll(getFileURLs(f));
                }
            }
        }
        return urls;
    }

    /**
     * File 转 URL
     *
     * @param file
     * @return
     */
    static URL buildURL(File file)
    {
        URL url = null;
        try
        {
            url = new URL("file:" + (!isUnix ? "/" : "")
                    + file.getAbsolutePath() + File.separator);
        } catch (MalformedURLException e)
        {
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
    private static File generateFile(String fileName, String java)
    {
        logger.info("Step 2:Write java string to file...");
        File file = new File(CLASSDIR + "/" + fileName + ".java");
        try
        {
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(java.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e)
        {
            logger.error("Write java strng to file fail.....message is {}",
                    e.getMessage());
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
    private static String parseClassName(String java)
    {
        logger.info("Step 1:Parse java string to get className...");
        int prefix_index = java.indexOf(" class ") + 7;
        int stufix_index1 = java.substring(prefix_index).indexOf(" ");
        int stufix_index2 = java.substring(prefix_index).indexOf("{");
        int stufix_index = stufix_index2 > stufix_index1
                ? stufix_index1
                : stufix_index2;
        String className = java.substring(prefix_index,
                prefix_index + stufix_index);
        className = className.replaceAll("\\s*", "");
        return className;
    }
    /**
     *
     * parse package name
     *
     **/
    private static String parsePackageName(String java)
    {
        java = java.replaceAll("(?>/\\*)[^(\\*/)]*\\*/", "");
        String packageName = java.substring(java.indexOf("package") + 8,
                java.indexOf(";"));
        return packageName;
    }
    public static ClassInfo parseClassInfo(String java)
    {
        String cn = parseClassName(java);
        String pn = parsePackageName(java);
        ClassInfo ci = new ClassInfo(cn, pn);
        return ci;
    }
}
package org.shoper.dynamiccompile;

        import com.sun.tools.javac.Main;
        import org.shoper.commons.MD5Util;
        import org.shoper.commons.StringUtil;
        import org.shoper.dynamiccompile.info.ClassInfo;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;

        import java.io.*;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.net.URLClassLoader;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Objects;
        import java.util.jar.Attributes;
        import java.util.jar.JarFile;
        import java.util.jar.Manifest;
        import java.util.zip.ZipEntry;

/**
 * Dynamic Compile and loader java.... Call method 动态编译加载
 *
 * @author ShawnShoper
 */
public class JDKCompile
{
    /**
     * class缓存验证类
     */
    private static ClassVerify cv = new ClassVerify();
    private static Logger logger = LoggerFactory.getLogger(JDKCompile.class);
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String SUB_DIR = "/tmp";
    private static final String CLASSDIR = USER_DIR + SUB_DIR;
    private static boolean isUnix = true;
    static URLClassLoader loader;
    // URLClassLoader.newInstance(buildClassPathURLs());
    // checking compile output directory....
    static
    {
        logger.info("Checking OS.....");
        String osName = System.getProperties().getProperty("os.name");
        if (osName.contains("Windows"))
        {
            logger.info("Current OS is windows...");
            isUnix = false;
        }
        logger.info("Checking compile output directory has exists....");
        File file = new File(CLASSDIR);
        if (!file.exists())
        {
            logger.info("Creating compile output directory ....");
            if (file.mkdirs())
            {
                logger.info("Compile output directory created");
            } else
            {
                logger.info(
                        "Create compile output directory fail,check the curr user has permission to write or right...compile output is {}",
                        CLASSDIR);
                throw new ExceptionInInitializerError(
                        "Create compile output directory fail,check the curr user has permission to write or right...compile output is "
                                + CLASSDIR);
            } ;
        }
        // loader = loadClassPath();
    }

    /**
     * Compile java file by java file String.
     *
     * @param file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static boolean compile(File file)
    {
        logger.info("Preparing to compile java file.....");
        int result = Main.compile(new String[]{"-d", CLASSDIR,
                CLASSDIR.substring(USER_DIR.length() + 1) + File.separator
                        + file.getName()});
        return result == 0 ? true : false;
    }
    /**
     * 检查当前内是否被缓存<br>
     * Created by ShawnShoper 2016年5月31日
     *
     * @param key
     * @param value
     * @return
     */
    private static boolean verify(String key, String value)
    {
        Object old = cv.getValue(key);
        if (Objects.isNull(old) || !old.equals(value))
            return false;
        else
            return true;
    }
    /**
     * 通过java文件获取class...
     *
     * @param code
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> getClass(final String code)
            throws ClassNotFoundException
    {
        loader = loadClassPath(buildClassPathURLs());
        ClassInfo ci = parseClassInfo(code);
        Class<?> clazz = null;
        String newz = MD5Util.GetMD5Code(code);
        String className = ci.getPackageName() + "." + ci.getClassName();
        if (verify(className, newz))
            clazz = loader.loadClass(className);
        else
        {
            clazz = getClass1(ci, code);
            cv.putClazzVerify(className, newz);
        }
        return clazz;
    }
    public static Class<?> getClass(File file)
            throws ClassNotFoundException, IOException
    {
        JarFile jarFile = null;
        String mainClass = null;
        URLClassLoader classLoader = null;
        InputStream inputStream = null;
        try
        {
            jarFile = new JarFile(file);
            ZipEntry zipEntry = jarFile.getEntry("META-INF/MANIFEST.MF");
            inputStream = jarFile.getInputStream(zipEntry);
            Manifest manifest = new Manifest(inputStream);
            mainClass = manifest.getMainAttributes()
                    .getValue(Attributes.Name.MAIN_CLASS);
            if (StringUtil.isEmpty(mainClass))
            {
                throw new IllegalAccessError(
                        "The jar file has not container manifest.mf");
            }
            classLoader = loadClassPath(file.toURI().toURL());
        } finally
        {
            if (Objects.nonNull(inputStream))
                inputStream.close();
            if (Objects.nonNull(jarFile))
                jarFile.close();
        }
        return classLoader.loadClass(mainClass);
    }
    public static void main(String[] args) throws Exception
    {
        Class<?> class1 = JDKCompile
                .getClass(new File("/Users/ShawnShoper/Desktop/a.jar"));
        Object object = class1.newInstance();
        class1.getMethod("A", null).invoke(object, null);
    }
    /**
     * 通过 class 文件获取类名<br>
     * Created by ShawnShoper 2016年5月31日
     *
     * @param is
     * @return
     * @throws IOException
     */
    // public static String getClassName(InputStream is) throws IOException
    // {
    // // 这部分属于 jdk 1.8,记得不要往下更换 JDK
    // ClassReader cr = new ClassReader(is);
    // String className = cr.getClassName();
    // return className.substring(className.lastIndexOf("/") + 1);
    // }
    /**
     * 通过 class 信息,以及源代码进行编译加载代码.</br>
     * 返回 class </br>
     * Created by ShawnShoper 2016年5月31日
     *
     * @param ci
     * @param main
     * @return
     * @throws ClassNotFoundException
     */
    private static Class<?> getClass1(ClassInfo ci, String main)
            throws ClassNotFoundException
    {
        Class<?> clazz = null;
        File file = generateFile(ci.getClassName(), main);
        if (compile(file))
        {

            loader = loadClassPath(buildClassPathURLs());
            loadInnerClass(ci);
            clazz = loader
                    .loadClass(ci.getPackageName() + "." + ci.getClassName());
        } else
            throw new RuntimeException(
                    "Compliing JAVA file failed...please check the java file content or user has permission to access or file is exists...the path is "
                            + CLASSDIR);
        return clazz;
    }

    /**
     * 加载相关内部类...
     *
     * @param ci
     * @throws ClassNotFoundException
     */
    private static void loadInnerClass(final ClassInfo ci)
            throws ClassNotFoundException
    {
        String classPath = USER_DIR + File.separator + SUB_DIR + File.separator
                + ci.getPackageName().replace(".", "/") + "/"
                + ci.getClassName();
        File file = new File(classPath);
        File[] files = file.getParentFile().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.startsWith(ci.getClassName() + "$");
            }
        });
        for (File f : files)
        {
            loader.loadClass(ci.getPackageName() + "."
                    + f.getName().substring(0, f.getName().indexOf(".")));
        }
    }

    /**
     * 重新加载classPath
     *
     * @return
     */
    private static URLClassLoader loadClassPath(URL[] urls)
    {
        loader = URLClassLoader.newInstance(urls,JDKCompile.class.getClassLoader());
        return loader;
    }
    private static URLClassLoader loadClassPath(URL url)
    {
        return loadClassPath(new URL[]{url});
    }
    public static URL[] buildClassPathURLs()
    {
        List<URL> urls = new ArrayList<URL>();
        // 构造file URL
        File file = new File(USER_DIR + SUB_DIR);
        urls.addAll(getFileURLs(file));
        return urls.toArray(new URL[]{});
    }

    /**
     * 获取需要动态加载的区域..扫描整个编译的临时目录...
     *
     * @param file
     * @return
     */
    private static List<URL> getFileURLs(File file)
    {
        if (file == null)
            throw new NullPointerException("The file must be not null..");
        List<URL> urls = new ArrayList<URL>();
        // 检查是否是隐藏文件,不扫描隐藏文件夹...
        {
            URL url = buildURL(file);
            if (url != null)
            {
                urls.add(url);
            }
        }
        for (File f : file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname)
            {
                return pathname.getName().startsWith(".") ? false : true;
            }
        }))
        {
            if (f.isDirectory())
            {
                URL url = buildURL(f);
                if (url != null)
                {
                    urls.addAll(getFileURLs(f));
                }
            }
        }
        return urls;
    }

    /**
     * File 转 URL
     *
     * @param file
     * @return
     */
    static URL buildURL(File file)
    {
        URL url = null;
        try
        {
            url = new URL("file:" + (!isUnix ? "/" : "")
                    + file.getAbsolutePath() + File.separator);
        } catch (MalformedURLException e)
        {
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
    private static File generateFile(String fileName, String java)
    {
        logger.info("Step 2:Write java string to file...");
        File file = new File(CLASSDIR + "/" + fileName + ".java");
        try
        {
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(java.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e)
        {
            logger.error("Write java strng to file fail.....message is {}",
                    e.getMessage());
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
    private static String parseClassName(String java)
    {
        logger.info("Step 1:Parse java string to get className...");
        int prefix_index = java.indexOf(" class ") + 7;
        int stufix_index1 = java.substring(prefix_index).indexOf(" ");
        int stufix_index2 = java.substring(prefix_index).indexOf("{");
        int stufix_index = stufix_index2 > stufix_index1
                ? stufix_index1
                : stufix_index2;
        String className = java.substring(prefix_index,
                prefix_index + stufix_index);
        className = className.replaceAll("\\s*", "");
        return className;
    }
    /**
     *
     * parse package name
     *
     **/
    private static String parsePackageName(String java)
    {
        java = java.replaceAll("(?>/\\*)[^(\\*/)]*\\*/", "");
        String packageName = java.substring(java.indexOf("package") + 8,
                java.indexOf(";"));
        return packageName;
    }
    public static ClassInfo parseClassInfo(String java)
    {
        String cn = parseClassName(java);
        String pn = parsePackageName(java);
        ClassInfo ci = new ClassInfo(cn, pn);
        return ci;
    }
}
