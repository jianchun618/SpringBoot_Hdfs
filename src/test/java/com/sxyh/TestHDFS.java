package com.sxyh;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileOutputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestHDFS {

    @Test
    public void test1() throws Exception{
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://CentOS8:8020");

        FileSystem fileSystem = FileSystem.get(conf);

        FSDataInputStream fsDataInputStream = fileSystem.open(new Path("/user/test.txt"));

        IOUtils.copyBytes(fsDataInputStream, System.out, 1024, true);
    }


    @Test
    public void test2() throws Exception {
        FileSystem fileSystem = getFileSystem();

        FSDataInputStream fsDataInputStream = fileSystem.open(new Path("/user/test.txt"));

        IOUtils.copyBytes(fsDataInputStream,System.out,1024,true);
    }

    @Test
    public void test3()throws Exception {
        Configuration conf = new Configuration();
        conf.addResource(new Path("C:\\Users\\Administrator\\IdeaProjects\\hadoop_code\\hadoop-hdfs-baizhiedu\\src\\main\\resources\\core-site.xml"));

        FileSystem fileSystem = FileSystem.get(conf);

        FSDataInputStream fsDataInputStream = fileSystem.open(new Path("/liuh/xiaohei/data"));

        IOUtils.copyBytes(fsDataInputStream, System.out, 1024, true);
    }

    @Test
    public void test4()throws Exception{
        FileSystem fileSystem = getFileSystem();

        FSDataInputStream fsDataInputStream = fileSystem.open(new Path("/liuh/xiaohei/data"));

        //System.out  换成 文件的输出流
        FileOutputStream fileOutputStream = new FileOutputStream("f://suns.txt");

        IOUtils.copyBytes(fsDataInputStream,fileOutputStream,1024,true);
    }

    @Test
    public void test5()throws Exception{
        //FileInputStream 读入文件内容
        FileInputStream fileInputStream = new FileInputStream("f://xiaojr.txt");
       //HDFS FileSystem.create();
        FileSystem fileSystem = getFileSystem();

        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/xiaohei/data"));

        //IOUtils IO处理
        IOUtils.copyBytes(fileInputStream,fsDataOutputStream,1024,true);
    }

    @Test
    public void test6()throws Exception{
        FileSystem fileSystem = getFileSystem();

        boolean isOk = fileSystem.mkdirs(new Path("/xiaojr"));

        System.out.println("创建目录 "+isOk);

        //fileSystem.delete(new Path(""),true);
    }

    @Test
    public void test7(){

        //String checksum = DigestUtils;
    }


    private FileSystem getFileSystem() throws Exception{
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://CentOS8:8020");
        conf.set("dfs.replication","1");

        FileSystem fileSystem = FileSystem.get(conf);

        return fileSystem;
    }

}
