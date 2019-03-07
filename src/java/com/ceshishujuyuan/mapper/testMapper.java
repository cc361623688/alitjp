package com.ceshishujuyuan.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


//这个目录，ceshishujuyuan（测试数据源），就是如果你的程序里是需要配置多数据源的，只需要在applicationContext.xml里
//把最下面的几个配置项改成你自己的目录，比如我就改成com.ceshishujuyuan了，再定义一个testMapper的变量就可以查其他数据源里的数据了


@Repository(value = "testMapper") 
public interface testMapper {
	List<Map<String,Object>> r(@Param("sql") String s);
} 
