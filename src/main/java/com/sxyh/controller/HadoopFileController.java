package com.sxyh.controller;

import com.sxyh.util.ExcelUtils;
import com.sxyh.util.HdfsUtil;
import com.sxyh.util.ThrowableUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FSDataInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/hadoop")
public class HadoopFileController {

    @Autowired
    private HdfsUtil hdfsUtil;

    //http://localhost:8080/hadoop/deleteFile?folderName=微信图片_20210708111636.jpg&rmdir=/user
    @RequestMapping(value = "/deleteFile", method = { RequestMethod.GET })
    public String deleteFile(HttpServletRequest request, @RequestParam("folderName")String folderName,
                             @RequestParam("rmdir")String rmdir) throws IOException{
        log.info("Delete File....");
        boolean rmdir1 = Boolean.parseBoolean(rmdir);
        String originFoldername = hdfsUtil.getHdfsProperties().getUploadPath() + folderName;
        //删除文件操作
        if(hdfsUtil.rmFile(originFoldername,rmdir1)){
            return "删除成功";
        }
        return "删除失败";
    }

    @RequestMapping(value = "/listFileStatus", method = { RequestMethod.GET })
    public Object listFileStatus(HttpServletRequest request) throws IOException{
        log.info("List FileStatus....");
        String folder = hdfsUtil.getHdfsProperties().getUploadPath();
        return hdfsUtil.lsFile(folder);
    }

    //http://localhost:8080/hadoop/uploadToHdfs
    @RequestMapping(value = "/uploadToHdfs", method = { RequestMethod.POST })
    public String uploadToHdfs(HttpServletRequest request, @RequestParam("file") MultipartFile file)
            throws IllegalStateException, IOException {
        log.info("uploadToHdfs started....");
        if (!file.isEmpty()) {
            log.info("file.size... " + file.getSize());
            try {
                String originalFilename = file.getOriginalFilename();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(originalFilename)));
                out.write(file.getBytes());
                out.flush();
                out.close();
                String destFileName = hdfsUtil.getHdfsProperties().getUploadPath() + originalFilename;

                log.info("destFileName::: " + destFileName);
                hdfsUtil.uploadFile(new String[] { originalFilename, destFileName });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error(ThrowableUtil.getErrorInfoFromThrowable(e));
                return "上传失败，" + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(ThrowableUtil.getErrorInfoFromThrowable(e));
                return "上传失败, " + e.getMessage();
            }
            return "上传成功";
        }
        return "上传失败，文件为空。";
    }

    //http://localhost:8080/hadoop/downloadFromHdfs
    @RequestMapping(value = "/downloadFromHdfs", method = { RequestMethod.GET })
    public String downloadFromHdfs(HttpServletRequest request, HttpServletResponse response, @RequestParam("filename")String fileName)
            throws IllegalStateException, IOException {
        log.info("downloadFromHdfs started....");
        log.info("------------------------------------------------------------");
        String originalFilename = hdfsUtil.getHdfsProperties().getUploadPath() + fileName;
        //String destFileName = localFilePath;
        //hdfs文件流读取文件
        FSDataInputStream in = hdfsUtil.downloadFile(new String[] { originalFilename});
        if(in == null){
            return  "下载失败,文件不存在";
        }

        //设置文件ContentType类型，自动判断下载文件类型
        response.setContentType("multipart/form-data");
        //通知浏览器以下载方式打开
        response.addHeader("Content-type", "appllication/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"),"ISO8859-1"));
        //获取文件输出
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        //循环取出流中的数据
        while((len = in.read(buffer)) != -1){
            out.write(buffer,0,len);
            out.flush();
        }
        out.close();
        log.info("------------------------------------------------------------");
        log.info("下载结束");

        return "下载成功";
    }

    /**
     * 新增解析excel文件功能
     * @param request
     * @param response
     * @param fileName
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/printFileInfo", method = { RequestMethod.POST })
    public String printFileInfo(HttpServletRequest request, HttpServletResponse response,@RequestParam("filename")String fileName) throws Exception {
        System.out.println("-----test-----");
        log.info("downloadFromHdfs started....");
        log.info("------------------------------------------------------------");
        String originalFilename = hdfsUtil.getHdfsProperties().getUploadPath() + fileName;
        //String destFileName = localFilePath;
        //hdfs文件流读取文件
        FSDataInputStream in = hdfsUtil.downloadFile(new String[] { originalFilename});
        if(in == null){
            return  "下载失败,文件不存在";
        }
        List courseListByExcel = ExcelUtils.getCourseListByExcel(in, originalFilename);
        System.out.println(courseListByExcel);

        return "------test-----";
    }
}
