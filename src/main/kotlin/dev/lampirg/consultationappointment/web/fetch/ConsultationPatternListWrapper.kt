package dev.lampirg.consultationappointment.web.fetch

import lombok.Data

@Data
open class ConsultationPatternListWrapper {
    var patterns: List<ConsultationPattern>? = null
}