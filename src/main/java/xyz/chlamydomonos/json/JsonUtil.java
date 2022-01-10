package xyz.chlamydomonos.json;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

/**
 * 一些Json序列化/反序列化工具
 */
public class JsonUtil
{
    /**
     * 整个项目使用的全局Gson对象
     */
    public static final Gson GSON = new Gson();

    /**
     * 从Json反序列化对象
     * @param json 对象序列化产生的Json
     * @param classOfT 对象的类型。注意，该类型必须存在反序列化的构造函数
     * @return 由Json构建出的反序列化对象
     * @see IJsonSerializable
     */
    @NotNull
    public static <T extends IJsonSerializable> T deserialize(@NotNull JsonObject json, @NotNull Class<? extends T> classOfT)
    {
        T temp;

        Constructor<? extends T> constructor;
        try
        {
            constructor = classOfT.getConstructor(JsonObject.class);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException("Class " + classOfT.getName() + " does not have a deserialize constructor!", e);
        }

        try
        {
            temp = constructor.newInstance(json);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            throw new RuntimeException("Create instance failed!", e);
        }

        return temp;
    }

    /**
     * 把Json对象保存到文件
     */
    public static void saveToFile(@NotNull JsonObject json, String fileName)
    {
        byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);

        try
        {
            File file = new File(fileName);
            if(!file.exists())
            {
                if(!file.getParentFile().exists())
                    if(!file.getParentFile().mkdirs())
                        throw new RuntimeException("What Happened??");

                if(!file.createNewFile())
                    throw new RuntimeException("What Happened??");
            }

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bufferedOutputStream.write(bytes, 0, bytes.length);
            bufferedOutputStream.close();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从文件读取Json对象
     */
    public static JsonObject loadFromFile(String fileName)
    {
        BufferedReader reader;
        StringBuilder builder = new StringBuilder();
        String line;
        try
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            line = reader.readLine();
            while (line != null)
            {
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return JsonUtil.GSON.fromJson(builder.toString(), JsonObject.class);
    }
}
