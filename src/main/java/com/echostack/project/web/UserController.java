package com.echostack.project.web;

import com.echostack.project.infra.dto.Result;
import com.echostack.project.infra.dto.ResultGenerator;
import com.echostack.project.model.entity.SysUser;
import com.echostack.project.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
* Created by CodeGenerator on 2019/02/21.
*/
@RestController
@RequestMapping("/user")
@Api(tags = {"会员"})
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ApiOperation(value = "新增用户", notes = "新增用户信息")
    public Result add(@ApiParam(required = true, value = "用户信息")
                          @RequestBody SysUser sysUser) {
        userService.add(sysUser);
        return ResultGenerator.genSuccessResult();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户", notes = "根据用户id删除用户")
    public Result delete(@ApiParam(required = true, value = "用户id",example = "123")
                             @PathVariable Long id) {
        userService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PutMapping
    @ApiOperation(value = "更新用户", notes = "更新用户信息")
    public Result update(@ApiParam(required = true, value = "用户信息")
                             @RequestBody SysUser sysUser) {
        sysUser.setGmtModified(new Date());
        userService.update(sysUser);
        return ResultGenerator.genSuccessResult();
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @GetMapping("/find")
    @ApiOperation(value = "查询用户信息", notes = "根据用户名称查询用户信息")
    public Result detail(@ApiParam(required = true, value = "用户名称")
                             @RequestParam String name) {
        SysUser sysUser = userService.findByUsername(name);
        return ResultGenerator.genSuccessResult(sysUser);
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_USER')")
    @GetMapping("/{id}")
    @ApiOperation(value = "查询用户信息", notes = "根据用户id查询用户信息")
    public Result detail(@ApiParam(required = true, value = "用户id",example = "123")
                         @PathVariable Integer id) {
//        SysUser sysUser = userService.findById(id);
        return ResultGenerator.genSuccessResult(null);
    }

    @GetMapping
    @ApiOperation(value = "分页查询用户信息", notes = "根据当前页码和每页大小分页查询用户信息")
    public Result list(@ApiParam(required = true, value = "当前页码",example = "1")
                           @RequestParam(defaultValue = "0") Integer page,
                       @ApiParam(required = true, value = "每页大小",example = "10")
                            @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysUser> list = userService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
