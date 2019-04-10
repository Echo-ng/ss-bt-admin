package com.echostack.project.web;

import com.echostack.project.infra.annotation.Logger;
import com.echostack.project.infra.dto.Result;
import com.echostack.project.infra.dto.ResultGenerator;
import com.echostack.project.model.entity.SysProperty;
import com.echostack.project.service.SysPropertyService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Echo
 * @Date: 2019/4/4 20:02
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/property")
@Api(tags = {"属性配置"})
public class SysPropertyController {

    @Autowired
    private SysPropertyService sysPropertyService;

    @PostMapping
    @ApiOperation(value = "新增属性", notes = "新增属性",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Logger("新增属性")
    public Result<SysProperty> add(@ApiParam(required = true, value = "属性信息")
                               @RequestBody SysProperty sysProperty) {
        try {
            sysPropertyService.add(sysProperty);
        } catch (Exception e) {
            String errorMsg = "新增属性错误，"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(sysProperty);
    }

    @PutMapping
    @ApiOperation(value = "更新属性", notes = "更新属性",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Logger("更新属性")
    public Result<SysProperty> update(@ApiParam(required = true, value = "属性信息")
                                  @RequestBody SysProperty sysProperty){
        try {
            sysPropertyService.update(sysProperty);
        } catch (Exception e) {
            String errorMsg = "更新属性错误，"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(sysProperty);
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除属性", notes = "删除属性",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Logger("删除属性")
    public Result<String> delete(@ApiParam(required = true, value = "属性信息",example = "123")
                                      @PathVariable Long id){
        try {
            sysPropertyService.deleteById(id);
        } catch (Exception e) {
            String errorMsg = "删除属性错误，"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult("删除成功");
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询属性信息,分页", notes = "查询属性信息,分页",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<PageInfo<List<SysProperty>>> findByPaging(@ApiParam(required = true, value = "页号",example = "1")
                                                                   @RequestParam Integer pageNum,
                                                                  @ApiParam(required = true, value = "页码",example = "10")
                                                                   @RequestParam Integer pageSize){
        PageInfo<List<SysProperty>> pageInfo;
        try {
            pageInfo = sysPropertyService.findByPaging(pageNum,pageSize);
        } catch (Exception e) {
            String errorMsg = "分页查询属性信息错误，"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping
    @ApiOperation(value = "根据属性id查询属性信息", notes = "根据属性id查询属性信息",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<SysProperty> findByPropId(@ApiParam(required = true, value = "属性key",example = "app.client.id")
                                            @RequestParam String propertyId){
        SysProperty sysProperty;
        try {
            sysProperty = sysPropertyService.findByPropId(propertyId);
        } catch (Exception e) {
            String errorMsg = "查询属性信息错误，"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(sysProperty);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询属性信息", notes = "根据id查询属性信息",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<SysProperty> findById(@ApiParam(required = true, value = "id",example = "123")
                                            @PathVariable Long id){
        SysProperty sysProperty;
        try {
            sysProperty = sysPropertyService.get(id);
        } catch (Exception e) {
            String errorMsg = "查询属性信息错误，"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(sysProperty);
    }

}
