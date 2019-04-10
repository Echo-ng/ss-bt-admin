package com.echostack.project.web;

import com.echostack.project.infra.annotation.Logger;
import com.echostack.project.infra.dto.Result;
import com.echostack.project.infra.dto.ResultGenerator;
import com.echostack.project.model.entity.SysDict;
import com.echostack.project.model.entity.SysDictValue;
import com.echostack.project.service.SysDictService;
import com.echostack.project.service.SysDictValueService;
import com.github.pagehelper.PageHelper;
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
 * @Date: 2019/4/3 16:23
 * @Description:
 */
@RestController
@RequestMapping("/dict")
@Api(tags = {"值列表"})
@Slf4j
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysDictValueService sysDictValueService;

    @PostMapping
    @ApiOperation(value = "新增值列表", notes = "新增值列表，如果值信息不为空的话，会关联新增值信息",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Logger
    public Result<SysDict> add(@ApiParam(required = true, value = "值列表信息")
                      @RequestBody SysDict sysDict) {
        try {
            sysDictService.add(sysDict);
        } catch (Exception e) {
            String errorMsg = "新增值列表失败"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(sysDict);
    }

    @PutMapping
    @ApiOperation(value = "更新值列表", notes = "更新值列表,如果值信息不为空的话，会关联更新值信息",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Logger
    public Result<SysDict> update(@ApiParam(required = true, value = "值列表信息")
                         @RequestBody SysDict sysDict){
        try {
            sysDictService.update(sysDict);
        } catch (Exception e) {
            String errorMsg = "更新值列表失败"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(sysDict);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询值列表信息", notes = "根据值列表id，查询值列表信息",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<SysDict> findById(@ApiParam(required = true, value = "值列表id",example = "123")
                           @PathVariable Long id){
        SysDict sysDict;
        try {
            sysDict = sysDictService.get(id);
        } catch (Exception e) {
            String errorMsg = "查询值列表失败"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(sysDict);
    }

    @GetMapping("/value")
    @ApiOperation(value = "根据id查询值列表信息,分页", notes = "根据值列表id，分页查询值列表信息",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<PageInfo<List<SysDictValue>>> findByDictIdPaging(@ApiParam(required = true, value = "值列表id",example = "123")
                                                        @RequestParam Long dictId,
                                                               @ApiParam(required = true, value = "页号",example = "1")
                                                         @RequestParam Integer pageNum,
                                                               @ApiParam(required = true, value = "页码",example = "10")
                                                             @RequestParam Integer pageSize){
        PageInfo<List<SysDictValue>> pageInfo;
        try {
            pageInfo = sysDictValueService.findByPaging(dictId,pageNum,pageSize);
        } catch (Exception e) {
            String errorMsg = "分页查询值列表失败"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping
    @ApiOperation(value = "分页查询所有值列表信息", notes = "分页查询所有值列表信息",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<PageInfo<List<SysDict>>> findByPaging(@ApiParam(required = true, value = "页号",example = "1")
                                                            @RequestParam Integer pageNum,
                                                        @ApiParam(required = true, value = "页码",example = "10")
                                                            @RequestParam Integer pageSize){
        PageInfo<List<SysDict>> pageInfo;
        try {
            pageInfo = sysDictService.findByPaging(pageNum,pageSize);
        } catch (Exception e) {
            String errorMsg = "分页查询值列表失败"+e.getMessage();
            log.error(errorMsg);
            return ResultGenerator.genFailResult(errorMsg);
        }
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
