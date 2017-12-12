package org.x.job.commons.increment;

/**
 * 雪花算法（Snowflake）
 * from Twitter
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 补码位 - 二进制毫秒位 - Master二进制数量位 - Slave二进制数量位 - 1毫秒最大Sequence值二进制数量位 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，取当前时间毫秒值。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点。<br>
 * 12位序列，毫秒内的计数，同一机器，同一时间截可产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * @author eightmonth
 */
public class Snowflake {
    public static final int NODE_SHIFT = 10;
    public static final int SEQ_SHIFT = 12;

    public static final short MAX_NODE = 1024; // 机器总数量
    public static final short MAX_SEQUENCE = 4096; // 毫秒最大数量

    private short sequence;  // 计数
    private long referenceTime; // 引用时间

    private int node;

    public Snowflake(int node) {
        if (node < 0 || node > MAX_NODE) {
            throw new IllegalArgumentException(String.format("node must be between %s and %s", 0, MAX_NODE));
        }
        this.node = node;
    }

    public long next() {

        long currentTime = System.currentTimeMillis();
        long counter;

        synchronized (this) {

            if (currentTime < referenceTime) {
                throw new RuntimeException(
                        String.format("Clock reversed! Last referenceTime %s is after reference time %s", referenceTime, currentTime));
            } else if (currentTime > referenceTime) {
                this.sequence = 0;
            } else {
                if (this.sequence < Snowflake.MAX_SEQUENCE) {
                    this.sequence++;
                } else {
                    throw new RuntimeException("Sequence exhausted at " + this.sequence);
                }
            }
            counter = this.sequence;
            referenceTime = currentTime;
        }

        return currentTime << NODE_SHIFT << SEQ_SHIFT | node << SEQ_SHIFT | counter;
    }

    public static void main(String[] args) {
        Snowflake sf = new Snowflake(1024);
        long id = sf.next();
        System.out.println(Long.toBinaryString(id)+"---"+Long.toBinaryString(id).length());//首位有个0，总长为64位
        System.out.println(id);
    }
}

