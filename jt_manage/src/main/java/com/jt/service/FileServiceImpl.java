package com.jt.service;

import com.jt.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService{

    @Value("${image.localDirPath}")
    private String localDirPath;    // = "D:/JT-SOFT/image";
    @Value("${image.urlPath}")
    private String urlPath;         // = "http://image.jt.com";


   /* private static Set<String> typeSet = new HashSet<>();

    //静态代码块  为成员变量赋值
    //可以利用配置文件方式 编辑所有的图片类型,通过循环遍历的方式
    static {
        typeSet.add("jpg");
        typeSet.add("png");
        typeSet.add("gif");
    }*/

    /**
     * 文件上传策略:
     *      1.图片类型的校验    1.正则表达式.. 2.???
     *      2.校验文件是否为木马   木马.exe.jpg 宽度/高度
     *      3.将文件分目录存储    1.hash asdfsdfa 2.时间
     *      4.防止文件重名  uuid当做名称
     * @param uploadFile
     * @return
     */
    @Override
    public ImageVO uploadFile(MultipartFile uploadFile) {
        //1.图片类型校验
        //1.1 获取图片名称      ABC.JPG
        String fileName = uploadFile.getOriginalFilename();
        //为了解决操作系统大小写问题 需要将字符全部小写
        fileName = fileName.toLowerCase();
        //1.2 截取图片类型 jpg|png|gif
        if(!fileName.matches("^.+\\.(jpg|png|gif)$")){
            return ImageVO.fail();
        }

        //2.是否为恶意程序
        try {
            //通过IO流读取文件信息,转化为图片类型
            BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            if(height == 0 || width == 0){
                //说明该文件是一个恶意程序
                return ImageVO.fail();
            }

            //3.根据时间动态生成文件目录
            String dateDirPath =
                    new SimpleDateFormat("/yyyy/MM/dd/").format(new Date());

            //3.1 实现用户本地路径的拼接 D:/JT-SOFT/images/2021/03/09/
            String localDir = localDirPath + dateDirPath;
            //3.2 动态生成文件路径
            File dirFile = new File(localDir);
            if(!dirFile.exists()){ //动态生成目录
                dirFile.mkdirs();  //创建多级目录
            }

            //4.动态生成用户名称  uuid.jpg
            String uuid = UUID.randomUUID().toString().replace("-", "");
            int index = fileName.lastIndexOf(".");
            String fileType = fileName.substring(index);
            String realFileName = uuid + fileType;

            //5. 实现文件上传  文件目录/文件名称
            File imageFile = new File(localDir + realFileName);
            uploadFile.transferTo(imageFile);

            //6.虚拟路径的拼接
            String url = urlPath + dateDirPath + realFileName;
            return ImageVO.success(url, width, height);
        } catch (IOException e) {
            e.printStackTrace();
            return ImageVO.fail();
        }
    }
}
