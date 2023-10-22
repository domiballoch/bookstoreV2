package dom.bookstore.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.test.web.servlet.ResultActions;

@UtilityClass
public class ControllerTestHelper {

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
}
