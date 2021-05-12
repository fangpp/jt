package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO implements Serializable {
    // {"error":0,"url":"图片的保存路径","width":图片的宽度,"height":图片的高度}
    private Integer error;      //0正常   1错误
    private String url;         //图片虚拟路径
    private Integer width;      //宽度
    private Integer height;     //高度

    public static ImageVO fail(){

        return new ImageVO(1, null, null, null);
    }

    public static ImageVO success(String url,Integer width,Integer height){

        return new ImageVO(0, url,width,height);
    }
}
