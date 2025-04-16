package com.app.conf;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义反序列化器，用于将JSON字符串转换为LocalDateTime对象。
 * 支持多种日期时间格式的解析。
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    // 定义支持的日期时间格式化器
    private static final DateTimeFormatter FORMATTER_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter FORMATTER_3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter FORMATTER_4 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter FORMATTER_5 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_6 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final DateTimeFormatter FORMATTER_7 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final DateTimeFormatter FORMATTER_8 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter FORMATTER_9 = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 将日期时间格式化器放入列表，便于管理和扩展
    private static final List<DateTimeFormatter> DATE_TIME_FORMATTERS = Arrays.asList(
            FORMATTER_1, FORMATTER_3, FORMATTER_5, FORMATTER_6, FORMATTER_7, FORMATTER_8
    );

    // 将日期格式化器放入列表，便于管理和扩展
    private static final List<DateTimeFormatter> DATE_FORMATTERS = Arrays.asList(
            FORMATTER_2, FORMATTER_4, FORMATTER_9
    );

    /**
     * 反序列化方法，将JSON字符串转换为LocalDateTime对象。
     *
     * @param p    JsonParser对象，用于读取JSON数据
     * @param ctxt DeserializationContext对象，提供反序列化上下文信息
     * @return 解析后的LocalDateTime对象
     * @throws IOException             如果发生I/O错误
     * @throws JsonProcessingException 如果JSON处理失败
     */
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String dateStr = p.getText();

        // 尝试使用日期时间格式化器解析
        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // 忽略异常，继续尝试下一个格式化器
            }
        }

        // 尝试使用日期格式化器解析，并将其转换为当天的开始时间
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate localDate = LocalDate.parse(dateStr, formatter);
                return localDate.atStartOfDay();
            } catch (DateTimeParseException e) {
                // 忽略异常，继续尝试下一个格式化器
            }
        }

        // 如果所有格式化器都无法解析，抛出异常并列出支持的格式
        throw new IOException("Date format not valid: " + dateStr + ". Supported formats: " +
                DATE_TIME_FORMATTERS.stream().map(DateTimeFormatter::toString).collect(java.util.stream.Collectors.toList()) + ", " +
                DATE_FORMATTERS.stream().map(DateTimeFormatter::toString).collect(java.util.stream.Collectors.toList()));

    }
}
