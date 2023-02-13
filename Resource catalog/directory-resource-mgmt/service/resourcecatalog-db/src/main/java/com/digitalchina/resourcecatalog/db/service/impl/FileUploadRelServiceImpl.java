package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.dao.SysStorageMapper;
import com.digitalchina.resourcecatalog.db.domain.FileUploadRel;
import com.digitalchina.resourcecatalog.db.dao.FileUploadRelMapper;
import com.digitalchina.resourcecatalog.db.domain.SysStorage;
import com.digitalchina.resourcecatalog.db.service.FileUploadRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件上传关联表 服务实现类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
@Service
public class FileUploadRelServiceImpl extends ServiceImpl<FileUploadRelMapper, FileUploadRel> implements FileUploadRelService {

    @Autowired
    SysStorageMapper sysStorageMapper;

    @Override
    public IPage<Map<String, Object>> page(IPage page, String name, String status, String startTime, String endTime, Integer userId, Integer deptId) {
        return this.baseMapper.page(page, name, status, startTime, endTime, userId, deptId);
    }

    @Override
    public Map<String, Object> detail(Integer id) {
        return this.baseMapper.detail(id);
    }

    @Override
    @Transactional
    public void updateInfo(FileUploadRel fileUploadRel) {
        FileUploadRel fileUploadRel1 = this.baseMapper.selectById(fileUploadRel.getId());
        if (fileUploadRel.getStorageId() != fileUploadRel.getStorageId()) {
            SysStorage sysStorage = new SysStorage();
            sysStorage.setId(fileUploadRel1.getStorageId());
            sysStorage.setDeleted(1);
            //文件逻辑删除
            sysStorageMapper.updateById(sysStorage);
        }
        this.baseMapper.updateById(fileUploadRel);
    }

    @Override
    public List<Map<String, Object>> waitReviewFileList(String status) {
        return this.baseMapper.waitReviewFileList(status);
    }

	@Override
	public String delete(Integer id) {
		int i=this.baseMapper.delete(new QueryWrapper<FileUploadRel>().eq("id", id));
		if(i==1){
			return "删除成功";
		}else{
			return "删除失败";
		}
	}
}
