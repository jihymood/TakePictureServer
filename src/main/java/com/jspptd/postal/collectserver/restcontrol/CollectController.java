package com.jspptd.postal.collectserver.restcontrol;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by LOG on 2017/7/5.
 */
@RestController
//@Controller
@RequestMapping("/takePicture")
public class CollectController {

    private Logger log = LoggerFactory.getLogger(CollectController.class);

    @Value("${destDirName}")
    protected String destDirName;

    /**
     * 图片上传
     */
    @RequestMapping(value = "/uploadPicture", method = RequestMethod.POST)
    ResponseObj<Boolean> uploadPicture(HttpServletRequest request) {
        log.info("进入文件上传逻辑");
        if (request instanceof MultipartHttpServletRequest) {
            try {
                MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
                Set<Map.Entry<String, MultipartFile>> set = mulRequest.getFileMap().entrySet();
                Map<String, InputStream> listFile = new LinkedHashMap<>();
                log.info("个数" + Integer.toString(set.size()));
                for (Map.Entry<String, MultipartFile> each : set) {
                    String fileName = each.getKey();
                    MultipartFile file = each.getValue();
                    //这里需要上传FTP
                    try {
                        listFile.put(fileName, file.getInputStream());
                    } catch (Exception ex) {
                        return new ResponseObj<>(false, new Error("文件存储失败"));
                    }
                }

                String formjson = mulRequest.getParameter("content");
                ObjectMapper mapper = new ObjectMapper();
                mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

//                boolean result = iInstallWorkOrder.upLoadFile(listFile);
                boolean result = true;

                return new ResponseObj<>(result);
            } catch (Exception ex) {
                return new ResponseObj<>(false, new Error(ex.getMessage()));
            }

        } else {
            return new ResponseObj<>(false, new Error("文件上传：数据格式不正确"));
        }
    }


    /**
     * 接收到安卓端传过来的map，将图片保存到本地电脑
     */
    @RequestMapping(value = "/picture", method = RequestMethod.POST)
    public ResponseObj<Boolean> reportCompany(HttpServletRequest request) {
        log.info("进入文件上传逻辑");
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
            /**
             * 下面的该方法适用单张图片的上传
             * 如果是多张图片一起上传，需要知道安卓端采用的是哪有多文件上传方式
             * 一：如果采用的是这种方式，则下面的方法不适用多文件上传
             *    @Multipart
             *    @POST("takePicture/picture")
             *    Observable<ResponseObj<Boolean>> uploadPictures(@Part("filename") String description,
             *    @Part("pic\"; filename=\"image1.png") RequestBody imgs1,
             *    @Part("pic\"; filename=\"image2.png") RequestBody imgs2);

             * 二：如果采用的是map这种方式，则可以
             *    @Multipart
             *    @POST("takePicture/picture")
             *    Observable<ResponseObj<Boolean>> uploadPictures(@Part("filename") String description,
             *    @PartMap() Map<String, RequestBody> maps);
             */
//            Set<Map.Entry<String, MultipartFile>> set = mulRequest.getFileMap().entrySet();
//            for (Map.Entry<String, MultipartFile> each : set) {
//                MultipartFile value = each.getValue();
//                String photoId = UUID.randomUUID().toString();
//                String eachKey = each.getKey();  //选择key作为文件名还是value里的，这里自己定义
//                String fileName = each.getValue().getOriginalFilename();  //图片流应该是在value里
//                String suffixal = getSuffixal(fileName);
//                try {
//                    writeToLocal(fileName, each.getValue().getInputStream());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return new ResponseObj(false, new Error("文件保存失败"));
//                }
//            }

            /**
             * 这种方法适用单张，多张图片上传，不管什么方式
             */
            Set<Map.Entry<String, List<MultipartFile>>> entries = mulRequest.getMultiFileMap().entrySet();
            for (Map.Entry<String, List<MultipartFile>> each : entries) {
                List<MultipartFile> value = each.getValue();
                for (MultipartFile multipartFile : value) {
                    String photoId = UUID.randomUUID().toString();
                    String eachKey = each.getKey();  //选择key作为文件名还是value里的，这里自己定义

                    String fileName = multipartFile.getOriginalFilename();
                    String suffixal = getSuffixal(fileName);
                    try {
                        writeToLocal(fileName, multipartFile.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new ResponseObj(false, new Error("文件保存失败"));
                    }
                }
            }

        }
        return new ResponseObj<>(true, null);
    }


    /**
     * 接收到安卓端传过来的map，将图片保存到本地电脑
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseObj<Boolean> uploadFile(HttpServletRequest request) {
        log.info("进入文件上传逻辑");
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
            Set<Map.Entry<String, MultipartFile>> set = mulRequest.getFileMap().entrySet();

            String photoId = UUID.randomUUID().toString();
            for (Map.Entry<String, MultipartFile> each : set) {
                String fileName = each.getKey();
                String name = each.getValue().getOriginalFilename();
                String suffixal = getSuffixal(name);
                try {
                    writeToLocal(fileName, each.getValue().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    return new ResponseObj(false, new Error("文件保存失败"));
                }
            }
        }
        return new ResponseObj<>(true, null);
    }


    /**
     * 将InputStream写入本地文件
     * 输入流
     *
     * @throws IOException
     */

    private void writeToLocal(String fileName, InputStream input) throws IOException {
        createDir(destDirName);
        String dirFile = destDirName + fileName;
        int len;
        byte[] bytes = new byte[1024];
        FileOutputStream fos = new FileOutputStream(dirFile);
        while ((len = input.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
            fos.flush();
        }
        fos.close();
        input.close();
        System.out.print("创建成功");
    }

    /**
     * 创建文件夹
     *
     * @param destDirName
     * @return
     */
    public boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {

            return true;
        } else {

            return false;
        }
    }

    /**
     * 截取图片后缀名
     */
    public String getSuffixal(String name) {
        String str = name.substring(name.length() - 4, name.length());
        return str;
    }

    /**
     * 获取当前时间
     */
    public long getNowTime() {
        return new Date().getTime();
    }

    /**
     * 判断String是否为空
     *
     * @param string
     * @return
     */
    public boolean isEmpty(String string) {
        return null == string || "".equals(string);
    }

}
