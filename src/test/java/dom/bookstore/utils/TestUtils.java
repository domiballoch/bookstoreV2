package dom.bookstore.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.ResultActions;

@UtilityClass
public class TestUtils {

    @SneakyThrows
    public static <R> R getResponseFrom(final ResultActions resultActions,
                                        final ObjectMapper objectMapper, final Class<R> responseType) {
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                responseType);
    }

    @SneakyThrows
    public static <R> R getResponseFrom(final ResultActions resultActions,
                                        final ObjectMapper objectMapper, final TypeReference<R> type) {
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(),
                type);
    }

    @SneakyThrows
    public static <T> String getJsonRequestFromClasspathAsString(String path, T t) {
        ObjectMapper objectMapper = new ObjectMapper();
        Object object = objectMapper.readValue(new ClassPathResource(path).getFile(), (Class) t);
        return objectMapper.writeValueAsString(object);
    }
}
