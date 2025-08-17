package alien.marshmallow.auth_service.config.logger;

import alien.marshmallow.auth_service.annotations.Sensitive;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import lombok.SneakyThrows;

public class SensitiveMaskingModule extends SimpleModule {

  @Override
  public void setupModule(SetupContext context) {
    context.addBeanSerializerModifier(new MaskingBeanSerializerModifier());
  }

  static final class MaskingBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> props) {
      for (int i = 0; i < props.size(); i++) {
        BeanPropertyWriter p = props.get(i);
        if (p.getAnnotation(Sensitive.class) != null) {
          props.set(i, new MaskingWriter(p));
        }
      }
      return props;
    }
  }

  static final class MaskingWriter extends BeanPropertyWriter {

    private static final String[] SYMBOLS = {
        "@", "#", "$", "%", "&", "!", "?", "💀", "🤬", "🔥", "💣", "👿", "🤯", "💥", "💩", "👽", "🤡", "🦄"};

    MaskingWriter(BeanPropertyWriter base) {
      super(base);
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
        throws Exception {
      Object value = get(bean);
      if (value == null && _nullSerializer == null && willSuppressNulls()) {
        return;
      }

      gen.writeFieldName(getName());
      if (value == null) {
        prov.defaultSerializeNull(gen);
        return;
      }
      gen.writeString(generateCensoredString(value.toString()));
    }

    @SneakyThrows
    private static String generateCensoredString(String input) {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < 8; i++) {
        int index = Byte.toUnsignedInt(hash[i]) % SYMBOLS.length;
        sb.append(SYMBOLS[index]);
      }

      return sb.toString();
    }
  }
}

