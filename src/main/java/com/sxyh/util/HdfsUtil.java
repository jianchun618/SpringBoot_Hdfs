package com.sxyh.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.GenericOptionsParser;

import com.sxyh.config.HdfsProperties;

/**
 * 参考：http://www.mamicode.com/info-detail-1719318.html
 *
 * @ClassName: HdfsUtil.java
 * @Description: 上传文件到hdfs
 * @author lc
 * @version V1.0
 * @Date 2019年8月22日 下午3:52:53
 */
@Slf4j
public class HdfsUtil {


	private HdfsProperties hdfsProperties;

	public HdfsUtil(HdfsProperties hdfsProperties) {
		this.hdfsProperties = hdfsProperties;
	}

	public HdfsProperties getHdfsProperties() {
		return hdfsProperties;
	}

	public static void main(String[] args) throws IOException {
		HdfsUtil hdfsUtil = new HdfsUtil(null);
		hdfsUtil.uploadFile("", "");
	}

	/**
	 * @Description: 获取hadoop系统配置
	 */
	public Configuration getConfig(){
		Configuration conf = new Configuration();
		// conf.setBoolean(CROSS_PLATFORM, true);
		log.info("host: " + hdfsProperties.getHost());
		log.info("defaultfs: " + hdfsProperties.getDefaultfs());
		conf.set(hdfsProperties.getDefaultfs(), hdfsProperties.getHost());
		return conf;
	}

	/**
	 * @Description: 上传文件到hdfs
	 */
	public void uploadFile(String... args) throws IOException {
		Configuration conf = getConfig();
		//命令行解析
		GenericOptionsParser optionsParser = new GenericOptionsParser(conf, args);
		String[] remainingArgs = optionsParser.getRemainingArgs();
		if (remainingArgs.length < 2) {
			System.err.println("Usage: upload <source> <dest>");
			System.exit(2);
		}

		Path source = new Path(args[0]);
		Path dest = new Path(args[1]);
		log.info("source::::: " + source.toUri().getPath());
		log.info("dest:::: " + dest.toUri().getPath());
		FileSystem fs = FileSystem.get(conf);

		fs.copyFromLocalFile(true, false, source, dest);
	}

	/**
	 * @Description: 下载文件到本地
	 */
	public FSDataInputStream downloadFile(String... args) throws IOException {
		Configuration conf = getConfig();
		GenericOptionsParser optionsParser = new GenericOptionsParser(conf, args);
		String[] remainingArgs = optionsParser.getRemainingArgs();
		if (remainingArgs.length < 2) {
			System.err.println("Usage: download <source> <dest>");
			//System.exit(2);
		}
		System.out.println(new File(args[0]));
		System.out.println(new File(args[0]).exists());
		Path source = new Path(args[0]);
		//Path dest = new Path(args[1]);

		log.info("source::::: " + source.toUri().getPath());
		//logger.info("dest:::: " + dest.toUri().getPath());
		FileSystem fs = FileSystem.get(conf);

		//判断文件是否存在
		if(fs.exists(source)){
			FSDataInputStream in = fs.open(source);
			return in;
		}
		return null;
		//fs.copyToLocalFile(source,dest);

	}

	/**
	 * @Description: 展示文件列表
	 */
	public Object lsFile(String folder)throws IOException {
		Configuration conf = getConfig();
		FileSystem fs = FileSystem.get(conf);

		//所在文件夹
		Path path = new Path(folder);
		FileStatus[] list = fs.listStatus(path);
		log.info("ls: " + folder);
		log.info("====================================");
		//文件列表
		List<FileList> fileList = new ArrayList<>();
		if (list != null)
			for (FileStatus f : list) {
				// System.out.printf("name: %s, folder: %s, size: %d\n",
				// f.getPath(), f.isDir(), f.getLen());
				log.info("" + f.getPath().getName() + ",folder: " + (f.isDirectory() ? "目录" : "文件") + ", 大小: " + f.getLen() / 1024 + "k");
				FileList list1 = new FileList(f.getPath().getName(),(f.isDirectory() ? "目录" : "文件"),f.getLen()/1024);
				fileList.add(list1);
			}
		log.info("====================================");
		fs.close();

		return JSON.toJSON(fileList);
	}

	/**
	 * @Description: 删除文件
	 */
	public boolean rmFile(String folder,boolean rmdir) throws IOException {
		Configuration conf = getConfig();
		FileSystem fs = FileSystem.get(conf);

		//所在文件或文件夹
		Path path = new Path(folder);
		log.info("ls: " + folder);
		log.info("====================================");
		if(!fs.exists(path)){
			log.info("文件或目录不存在");
			log.info("====================================");
			return false;
		}
		if(!rmdir){
			if(fs.isDirectory(path)){
				log.info("can not delete directory");
				log.info("====================================");
				return false;
			}else {
				fs.delete(path,rmdir);
			}
		}else{
			fs.delete(path,rmdir);
		}
		log.info("====================================");
		return true;

	}
}
