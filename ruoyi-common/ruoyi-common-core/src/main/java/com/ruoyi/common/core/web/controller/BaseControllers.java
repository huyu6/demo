//package com.ruoyi.common.core.web.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.example.reportclient.utils.JwtUtil;
//import com.example.reportclient.utils.MapUtil;
//import com.example.reportclient.utils.StringUtil;
//import io.swagger.v3.oas.annotations.Hidden;
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.Cleanup;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.lang.reflect.Field;
//import java.util.*;
//
///**
// * @param <M>
// * @param <T>
// * @Api → @Tag
// * @ApiIgnore → @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
// * @ApiImplicitParam → @Parameter
// * @ApiImplicitParams → @Parameters
// * @ApiModel → @Schema
// *@Schema(name =hidden = true) → @Schema(accessMode = READ_ONLY)
// * @ApiModelProperty → @Schema
// * @ApiOperation(value = "foo", notes = "bar") → @Operation(summary = "foo", description = "bar")
// * @ApiParam → @Parameter
// * @ApiResponse(code = 404, message = "foo") → @ApiResponse(responseCode = "404", description = "foo")
// */
//public class BaseController<M extends IService<T>, T> {
//    @Autowired(required = false)
//    protected M service;
//    @Hidden
//    @Operation(description = "添加")
//    @PostMapping
//    public ResponseResult save(@RequestBody T t) {
//        service.saveOrUpdate(t);
//        return ResponseResult.success("ok");
//    }
//    @Hidden
//    @Operation(description = "修改")
//    @PutMapping
//    public ResponseResult update(@RequestBody T t) {
//        service.saveOrUpdate(t);
//        return ResponseResult.success("ok");
//    }
//    @Hidden
//    @Operation(description = "根据id删除")
//    @PostMapping("deleteById/{id}")
//    public ResponseResult deleteById(@PathVariable("id") Integer id) {
//        service.removeById(id);
//        return ResponseResult.success("ok");
//    }
//    @Hidden
//    @Operation(description = "根据id删除")
//    @PostMapping("delete/batch")
//    public ResponseResult deleteBatch(@RequestBody List<Long> ids) {
//        service.removeBatchByIds(ids);
//        return ResponseResult.success("ok");
//    }
//
//    @Hidden
//    @Operation(description = "根据id获取")
//    @GetMapping("{id}")
//    public ResponseResult<T> get(@PathVariable("id") Integer id) {
//        T t = service.getById(id);
//        return ResponseResult.success("ok", t);
//
//    }
//
//
//    /**
//     * 分页获取
//     *
//     * @param orderField  排序字段名称 如id
//     * @param order       排序方式 降序descend ，升序ascend
//     * @param likeField   模糊查询字段，多个用逗号分割 如name,nick
//     * @param keyword     模糊查询值 如张三
//     * @param param       等于的查询条件 sex=1
//     * @param selectField 结果指定的字段如 id,name,age
//     */
//    //            query.and(wrapper -> wrapper.eq("from_user", userId).or().eq("to_user", userId));
//    @Hidden
//    @Operation(description = "获取分页")
//    @GetMapping("pageList")
//    public ResponseResult<Page<T>> page(@RequestParam(required = false, defaultValue = "1") int pageNumber,
//                                        @RequestParam(required = false, defaultValue = "10") int pageSize,
//                                        @RequestParam(required = false) String orderField,
//                                        @RequestParam(required = false, defaultValue = "descend") String order,
//                                        @RequestParam(required = false) String likeField,
//                                        @RequestParam(required = false) String keyword,
//                                        @RequestParam(required = false) String selectField,
//                                        T param) {
//
//        //where
//        QueryWrapper<T> queryWrapper = getQuery(orderField, order, likeField, keyword, param, selectField);
//        //page
//        Page<T> dataPage = new Page<>(pageNumber, pageSize);
//        service.page(dataPage, queryWrapper);
//        return ResponseResult.success("ok", dataPage);
//    }
//
//    public QueryWrapper<T> getQuery(String orderField, String order, String likeField, String keyword, T param, String selectField) {
//        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
//        if (StringUtil.isNotBlank(selectField)) {
//            String[] selects = selectField.split(",");
//            for (int i = 0; i < selects.length; i++) {
//                selects[i] = StringUtil.camelToUnderline(selects[i]);
//            }
//            queryWrapper.select(selects);
//        }
//        if (param != null) {
//            Map<String, Object> map = null;
//            try {
//                map = MapUtil.objectToDataMap(param, false);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//
//            //like
//            if (likeField != null && StringUtils.hasLength(keyword)) {
//                String[] likes = likeField.split(",");
//                if (likes.length == 1) {
//                    queryWrapper.like(likes[0], keyword);
//                } else {
//                    queryWrapper.and(wrapper -> {
//                        for (String lf : likes) {
//                            String filed = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(lf);
//                            wrapper.or().like(filed, keyword);
//                        }
//                    });
//                }
//            }
////                if(map.get("create_time")!=null){
////                    //处理时间筛选
////                    queryWrapper.likeRight("create_time",map.get("create_time"));
////                    map.put("create_time", null);
////                }
//
//            //eq
//            map.entrySet().removeIf(entry -> entry.getValue() == null || entry.getValue() == "");
//            if (!map.isEmpty()) {
//                queryWrapper.allEq(map, false);
//            }
//        }
//        //order
//        if (StringUtils.hasLength(orderField)) {
//            orderField = com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(orderField);
//            if ("descend".equals(order)) {
//                queryWrapper.orderByDesc(orderField);
//            } else if ("ascend".equals(order)) {
//                queryWrapper.orderByAsc(orderField);
//            }
//        }
//        return queryWrapper;
//    }
//    @Hidden
//    @Operation(description = "获取列表")
//    @GetMapping("list")
//    public ResponseResult<List<T>> list(@RequestParam(required = false) String orderField,
//                                        @RequestParam(required = false, defaultValue = "descend") String order,
//                                        @RequestParam(required = false) String likeField,
//                                        @RequestParam(required = false) String searchWord,
//                                        @RequestParam(required = false) String selectField,
//                                        @RequestParam(required = false, defaultValue = "999") int limit,
//                                        T param) {
//        QueryWrapper<T> queryWrapper = getQuery(orderField, order, likeField, searchWord, param, selectField)
//                .last("limit " + limit);
//        List<T> list = service.list(queryWrapper);
//        return ResponseResult.success("ok", list);
//    }
//
//    /**
//     * 通用导出
//     *
//     * @param exportHead  导出表头 如 年龄,姓名
//     * @param exportField 对应导出字段，如 age,name
//     * @param count       导出条数，默认1000
//     */
//    @Hidden
//    @Operation(description = "导出")
//    @GetMapping("exportList")
//    public void exportList(@RequestParam(required = false) String orderField,
//                           @RequestParam(required = false, defaultValue = "descend") String order,
//                           @RequestParam(required = false) String likeField,
//                           @RequestParam(required = false) String keyword,
//                           T param,
//                           @RequestParam(required = false) String exportHead,
//                           @RequestParam(required = false) String exportField,
//                           @RequestParam(required = false, defaultValue = "1000") int count,
//                           HttpServletResponse response) {
//        QueryWrapper<T> queryWrapper = getQuery(orderField, order, likeField, keyword, param, exportField);
//        List<T> lists = service.list(queryWrapper.last("limit " + count));
//        if (lists == null || lists.size() == 0) {
//            return;
//        }
//        try {
//            List<List<Object>> exData = new ArrayList<>(lists.size());
//            String[] headers = exportHead.split(",");
//            String[] fields = exportField.split(",");
//            for (T entity : lists) {
//                List<Object> list = new ArrayList<>();
//                Class c = entity.getClass();
//                for (String filed : fields) {
//                    Field field = c.getDeclaredField(filed);
//                    field.setAccessible(true);
//                    Object value = field.get(entity);
//                    list.add(value);
//                }
//                exData.add(list);
//            }
//            String name = param.getClass().getSimpleName();
//            // ExportExcelUtil.exportExcel(response, name + "表", Arrays.asList(headers), exData, name, null);
//        } catch (Exception e) {
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("application/json; charset=utf-8");
//            @Cleanup PrintWriter writer = null;
//            try {
//                writer = response.getWriter();
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
//            ResponseResult res = ResponseResult.error("系统异常");
//            String s = JSON.toJSONString(res);
//            writer.write(s);
//            e.printStackTrace();
//        }
//    }
//
//    public static Integer getTokenUserId(HttpServletRequest req) {
//        String token = req.getHeader(HttpHeaders.AUTHORIZATION);
//        if (token != null) {
//            Integer userId = null;
//            try {
//                DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
//                assert decodedJWT != null;
//                Integer id = decodedJWT.getClaim("userId").asInt();
//            } catch (Exception e) {
//            }
//            return userId;
//        }
//        return null;
//    }
//}
