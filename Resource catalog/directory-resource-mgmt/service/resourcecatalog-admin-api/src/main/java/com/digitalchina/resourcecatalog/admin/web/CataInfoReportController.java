package com.digitalchina.resourcecatalog.admin.web;

import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.BUS_ERROR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.storage.StorageService;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.CataInfoReport;
import com.digitalchina.resourcecatalog.db.domain.SysStorage;
import com.digitalchina.resourcecatalog.db.service.CataInfoReportService;
import com.digitalchina.resourcecatalog.db.service.SysStorageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 信息资源目录报告 前端控制器
 * </p>
 *
 * @author baokd
 * @since 2020-05-25
 */
@RestController
@RequestMapping("/admin/cataInfoReport")
@Api(value = "信息资源目录报告", tags = "信息资源目录报告")
public class CataInfoReportController {
	@Autowired
	CataInfoReportService cataInfoReportService;
	@Autowired
	SysStorageService sysStorageService;
	@Autowired
	StorageService storageService;
	@Autowired
	private LogHelper logHelper;

	@RequiresPermissions("cata:infoReport:list")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录报告" }, button = "分页查询")
	@GetMapping("/list")
	@ApiOperation("分页查询")
	public Object list(@RequestParam Integer depId,
			@RequestParam(required = false) @ApiParam(value = "开始日期，格式yyyy-mm-dd") String startDate,
			@RequestParam(required = false) @ApiParam(value = "结束日期，格式yyyy-mm-dd") String endDate,
			@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
		Page page1 = new Page<>(page, limit);
		return ResponseUtil.ok(cataInfoReportService.selectPages(page1,depId,startDate,endDate));
	}

	/***
	 * 删除
	 * 
	 * @return
	 */
	@GetMapping("/delete")
	@RequiresPermissions("cata:infoReport:delete")
	@RequiresPermissionsDesc(menu = { "目录编制","信息资源目录报告" }, button = "删除")
	@ApiOperation("删除")
	public Object delete(@RequestParam Integer id) {
		CataInfoReport cataInfoReport = cataInfoReportService.getById(id);
		// 逻辑删除能查到记录
		if (null != cataInfoReport) {
			cataInfoReportService.delete(cataInfoReport);
			SysStorage sysStorage = sysStorageService
					.getOne(new QueryWrapper<SysStorage>().eq(SysStorage.KEY, cataInfoReport.getFileKey()));
			if (null != sysStorage) {
				logHelper.logGeneralSucceed("信息资源目录报告", "删除信息资源目录报告记录【" + sysStorage.getName() + "】。");
			}
		}else{
			return ResponseUtil.fail(BUS_ERROR,"删除失败");
		}
		return ResponseUtil.ok();
	}

	/***
	 * 生成报告
	 */
	@GetMapping("/generateReport")
	@RequiresPermissions("cata:infoReport:generateReport")
	@RequiresPermissionsDesc(menu = { "目录编制", "信息资源目录报告" }, button = "生成报告")
	@ApiOperation("生成报告")
	public Object generateReport(@RequestParam(required = true) Integer depId) {
		String filePath = cataInfoReportService.generateReport(depId);
		if (filePath == null) {
			return ResponseUtil.fail(BUS_ERROR, "生成失败");
		}
		File file = new File(filePath);
		FileInputStream files = null;
		try {
			files = new FileInputStream(file);
			String originalFilename = file.getName();
			SysStorage sysStorage = storageService.store(files, file.length(),
					"application/vnd.openxmlformats-officedocument.wordprocessingml.document", originalFilename);
			if (sysStorage != null) {
				CataInfoReport cataInfoReport = new CataInfoReport();
				cataInfoReport.setDepId(depId);
				cataInfoReport.setDeleted(0);
				cataInfoReport.setDownloadTime(new Date());
				cataInfoReport.setFileKey(sysStorage.getKey());
				cataInfoReport.setReportName(originalFilename);
				cataInfoReport.setDownloadBy(UserInfo.getName());
				cataInfoReport.setCreateBy(UserInfo.getUsername());
				cataInfoReportService.save(cataInfoReport);
				logHelper.logGeneralSucceed("信息资源目录报告", "导出信息资源报告【"
						+ originalFilename.split("-")[1].substring(0, originalFilename.split("-")[1].lastIndexOf("."))
						+ "】。");
				return ResponseUtil.ok(sysStorage.getKey());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (files != null) {
				try {
					files.close();
					file.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ResponseUtil.fail(BUS_ERROR, "生成失败");
	}
}
