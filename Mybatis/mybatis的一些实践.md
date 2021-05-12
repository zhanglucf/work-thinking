### 1、使用原生的mybatis,手写sql时如何自动类型转换

> 需求：某个字段类型为Map,在我们存储的时候，希望自动转成Json字符串。在我们取数据时，再次自动转换成Map

#### 第一步、自定义类型转换器

> 实现 `org.apache.ibatis.type`包下的`TypeHandler<T>`接口

```java
package com.cetccloud.handler;

import com.cetccloud.util.CGSON;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author ZhangZhenhua
 * @date 2021/2/24 16:04
 */
public class MapHandler implements TypeHandler<Map> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Map parameter, JdbcType jdbcType) throws SQLException {
        String json = CGSON.toJson(parameter);
        ps.setString(i,json);
    }

    @Override
    public Map getResult(ResultSet rs, String columnName) throws SQLException {
        String string = rs.getString(columnName);
        Map map = CGSON.fromJson(string, Map.class);
        return map;
    }

    @Override
    public Map getResult(ResultSet rs, int columnIndex) throws SQLException {
        String string = rs.getString(columnIndex);
        Map map = CGSON.fromJson(string, Map.class);
        return map;
    }

    @Override
    public Map getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String string = cs.getString(columnIndex);
        Map map = CGSON.fromJson(string, Map.class);
        return map;
    }
}

```



#### 第二步、指定类型转换器的完整类名

> 在mapper.xml 的sql片段中指定类型转换器的完整类名  `typeHandler=com.cetccloud.handler.MapHandler`

```xml
    <insert id="insertMeeting" parameterType="com.cetccloud.entity.meeting.Meeting">
        insert into t_pr_meeting
        values (#{meeting.id},
        #{meeting.meetingNum},
        #{meeting.meetingName},
        #{meeting.meetingDateStart},
        #{meeting.meetingDateEnd},
        #{meeting.meetingSummarize},
        #{meeting.meetState},
        #{meeting.projectType},
        #{meeting.project,typeHandler=com.cetccloud.handler.MapHandler},
        #{meeting.auditTime},
        #{meeting.auditLocation},
        #{meeting.auditUnit} ,
        #{meeting.insertTime},
        #{meeting.insertBy},
        #{meeting.updateTime},
        #{meeting.updateBy});
    </insert>
```



### 2、使用通用mapper时如何自动类型转换

#### 第一步、自定义类型转换器

> 实现 `org.apache.ibatis.type`包下的`TypeHandler<T>`接口

```java
package com.cetccloud.handler;

import com.cetccloud.util.CGSON;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author ZhangZhenhua
 * @date 2021/2/24 16:04
 */
public class MapHandler implements TypeHandler<Map> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Map parameter, JdbcType jdbcType) throws SQLException {
        String json = CGSON.toJson(parameter);
        ps.setString(i,json);
    }

    @Override
    public Map getResult(ResultSet rs, String columnName) throws SQLException {
        String string = rs.getString(columnName);
        Map map = CGSON.fromJson(string, Map.class);
        return map;
    }

    @Override
    public Map getResult(ResultSet rs, int columnIndex) throws SQLException {
        String string = rs.getString(columnIndex);
        Map map = CGSON.fromJson(string, Map.class);
        return map;
    }

    @Override
    public Map getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String string = cs.getString(columnIndex);
        Map map = CGSON.fromJson(string, Map.class);
        return map;
    }
}

```

#### 第二步、在实体类上指定类型转换器

> `@ColumnType(typeHandler = MapHandler.class)`

```java
    @ColumnType(typeHandler = MapHandler.class)
    private Map<String,Object> project;
```



### 3、在mybatis中使用枚举

#### 第一步、定义枚举类的通用接口

> 通用接口目的在于抽出枚举转换成int 和int转换成枚举的通用方法，以供后面写通用类型转换器时使用

```java
public interface BaseEnum {

    int getValue();

    String getDesc();

    static <E extends Enum<E> & BaseEnum> E intToEnum(int value, Class<E> clazz) {
        EnumSet<E> enumSet = EnumSet.allOf(clazz);
        return enumSet.stream().filter(x -> value == x.getValue()).findFirst().orElse(null);
    }

    static <E extends Enum<E> & BaseEnum> E create(String type, Class<E> clazz) {
        EnumSet<E> enumSet = EnumSet.allOf(clazz);
        return enumSet.stream().filter(x -> type.equals(""+ x.getValue())  || type.equals(x.getDesc())).findFirst().orElse(null);
    }

}
```

#### 第二步、定义枚举类

> 枚举类中的 `@JsonValue @JsonCreator` 注解 以及方法 `public static MeetingStateEnum create(String value){...}` 具体的左右会在springmvc枚举实践中在做详细说明，这里和mybatis使用没有关系。

```java
package com.cetccloud.constant;

import com.cetccloud.handler.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MeetingStateEnum implements BaseEnum {
    NOT_START(0, "notStart", "未开始"),
    UNDERWAY(1, "underway", "进行中"),
    COMPLETE(2, "complete", "完成");

    private Integer value;
    private String desc;
    private String descCN;

    MeetingStateEnum() {}

    MeetingStateEnum(Integer value, String desc, String descCN) {
        this.value = value;
        this.desc = desc;
        this.descCN = descCN;
    }

    @JsonValue
    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getDesc() {
        return this.desc;
    }


    @JsonCreator
    public static MeetingStateEnum create(String value) {
        try {
            return MeetingStateEnum.valueOf(value);
        } catch (IllegalArgumentException e) {
            for (MeetingStateEnum meetingStateEnum : MeetingStateEnum.values()) {
                try {
                    if (meetingStateEnum.value == (Integer.parseInt(value))) {
                        return meetingStateEnum;
                    }
                } catch (NumberFormatException n) {
                    if (meetingStateEnum.desc.equals(value)) {
                        return meetingStateEnum;
                    }
                }
            }
            throw new IllegalArgumentException("No element matches " + value);
        }

    }

    @Override
    public String toString() {
        return this.value + "";
    }
}

```



#### 第三步、定义类型处理器

> 这里是经过多次尝试，实践出来的通用写法，不必为每个具体的枚举一一配置类型转换器

```java
package com.cetccloud.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseEnumTypeHandler<E extends Enum<E> & BaseEnum> extends BaseTypeHandler<E> {

    private Class<E> type;

    public BaseEnumTypeHandler() {}

    public BaseEnumTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, Integer.valueOf(parameter.getValue()));
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return get(rs.getString(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return get(rs.getString(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return get(cs.getString(columnIndex));
    }

    private <E extends Enum<E> & BaseEnum> E get(String v) {
        if (v == null) return null;
        if (StringUtils.isNumeric(v)) {
            return (E) BaseEnum.intToEnum(Integer.valueOf(v), type);
        } else {
            return (E) BaseEnum.create(v, type);
        }
    }
}

```



#### 第四步、指定枚举的通用类型转换器

> 在spring配置文件中指定枚举的通用类型转换器 `default-enum-type-handler: com.cetccloud.handler.BaseEnumTypeHandler`

```yaml
mybatis:
  configuration:
    default-enum-type-handler: com.cetccloud.handler.BaseEnumTypeHandler
```



