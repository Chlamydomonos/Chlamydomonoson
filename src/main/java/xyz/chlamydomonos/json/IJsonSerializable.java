package xyz.chlamydomonos.json;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

/**
 * 可被自定义Json序列化的对象
 * <br>
 * 这个接口允许对象被序列化为Json。
 * 注意，在实现此接口时，实现类需要具有一个用于从Json反序列化的构造函数。
 */
public interface IJsonSerializable
{
    /**
     * 序列化
     * <br>
     * 在此函数中创建Json对象并把属性写入其中，以完成序列化
     */
    @NotNull
    JsonObject serialize();
}
