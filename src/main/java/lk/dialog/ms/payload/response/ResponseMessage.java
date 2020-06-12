/**
 * The MessageResponse use to bundle response information.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.payload.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {

    private Integer httpStatus;
    private Object message;
    private Integer code;
    private String type;
    private String origin;
    private String originalMessage;
    private String details;
    private String traceId;
    private Date timestamp;

    @NoArgsConstructor
    public static class ResponseBuilder {
        private Integer httpStatus;
        private Object message;
        private Integer code;
        private String type;
        private String origin;
        private String originalMessage;
        private String details;
        private String traceId;
        private Date timestamp;

        public ResponseBuilder setHttpStatus(Integer httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ResponseBuilder setMessage(Object message) {
            this.message = message;
            return this;
        }

        public ResponseBuilder setCode(Integer code) {
            this.code = code;
            return this;
        }

        public ResponseBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public ResponseBuilder setOrigin(String origin) {
            this.origin = origin;
            return this;
        }

        public ResponseBuilder setOriginalMessage(String originalMessage) {
            this.originalMessage = originalMessage;
            return this;
        }

        public ResponseBuilder setDetails(String details) {
            this.details = details;
            return this;
        }

        public ResponseBuilder setTraceId(String traceId) {
            this.traceId = traceId;
            return this;
        }

        public ResponseBuilder setTimeStamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ResponseMessage build() {
            return new ResponseMessage(httpStatus, message, code, type, origin, originalMessage, details, traceId,
                                       timestamp);
        }
    }
}
