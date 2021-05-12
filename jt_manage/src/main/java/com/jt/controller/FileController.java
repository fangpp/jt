package com.jt.controller;

import com.jt.service.FileService;
import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController //表示返回数据是一个JSON
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 业务说明: 实现用户文件上传
     * url地址:   http://localhost:8091/file
     * 请求参数:  fileImage
     * 返回值:    操作成功的字符串
     * 文件上传的容量:默认的条件下 1M
     */
    @RequestMapping("/file")
    public String file(MultipartFile fileImage) throws IOException {
        String dir = "D:/JT-SOFT/images";
        File dirFile = new File(dir);
        if(!dirFile.exists()){
            dirFile.mkdirs();   //表示创建多级目录
        }
        //获取文件名称
        String fileName = fileImage.getOriginalFilename();
        //将文件封装为一个完整的路径
        File imageFile = new File(dir+"/"+fileName);
        //接口中提供的一个方法  实现文件上传. outputStream 实现写盘操作.
        fileImage.transferTo(imageFile);
        return "文件上传成功!!!";
    }

    /**
     * 业务实现: 文件上传实现
     * URL地址:   http://localhost:8091/pic/upload?dir=image
     * 参数:      uploadFile
     * 返回值:    ImageVO
     */
    @RequestMapping("/pic/upload")
    public ImageVO uploadFile(MultipartFile uploadFile){

        return fileService.uploadFile(uploadFile);
       /* String url = "https://img14.360buyimg.com/n0/jfs/t1/151857/40/2398/38943/5f8842b9Edac7df98/0a8a77bfea24aa1d.jpg";
        return ImageVO.success(url, 800, 800);*/
    }
}
