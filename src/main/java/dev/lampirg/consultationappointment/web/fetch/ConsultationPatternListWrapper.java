package dev.lampirg.consultationappointment.web.fetch;

import lombok.Data;
import java.util.List;

@Data
public class ConsultationPatternListWrapper {
    List<ConsultationPattern> patterns;
}
