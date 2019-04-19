package com.hpe.service.impl;

import com.hpe.entity.File;
import com.hpe.mapper.FileMapper;
import com.hpe.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zxj
 * @since 2019-04-09
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

}
