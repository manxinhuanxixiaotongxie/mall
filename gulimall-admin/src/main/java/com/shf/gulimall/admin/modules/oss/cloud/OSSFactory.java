/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.shf.gulimall.admin.modules.oss.cloud;


import com.shf.gulimall.admin.common.utils.ConfigConstant;
import com.shf.gulimall.admin.common.utils.Constant;
import com.shf.gulimall.admin.common.utils.SpringContextUtils;
import com.shf.gulimall.admin.modules.sys.service.SysConfigService;

/**
 * 文件上传Factory
 *
 * @author Mark sunlightcs@gmail.com
 */
public final class OSSFactory {
    private static SysConfigService sysConfigService;

    static {
        OSSFactory.sysConfigService = (SysConfigService) SpringContextUtils.getBean("sysConfigService");
    }

    public static CloudStorageService build() {
        //获取云存储配置信息
        CloudStorageConfig configObject = sysConfigService.getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY,
                CloudStorageConfig.class);

        if (configObject.getType() == Constant.CloudService.QINIU.getValue()) {
            return new QiniuCloudStorageService(configObject);
        } else if (configObject.getType() == Constant.CloudService.ALIYUN.getValue()) {
            return new AliyunCloudStorageService(configObject);
        } else if (configObject.getType() == Constant.CloudService.QCLOUD.getValue()) {
            return new QcloudCloudStorageService(configObject);
        }

        return null;
    }

}
