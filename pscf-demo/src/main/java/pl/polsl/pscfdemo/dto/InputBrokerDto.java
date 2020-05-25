package pl.polsl.pscfdemo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Data
@Builder
public class InputBrokerDto {

    @JsonProperty("pH")
    private Double phValue;

    @JsonProperty("temp")
    private Double temperature;

    @JsonProperty("proc")
    private Double percentageOfChemicals;

    @JsonProperty("dawka")
    private Double dose;

    @JsonProperty("bOdwroconaOsmoza")
    private Boolean reverseOsmosis;

    @JsonProperty("bFiltrWeglowyOk")
    private Boolean carbonFilter;

    @JsonProperty("bFiltrZwirowyOk")
    private Boolean gravelFilter;

    @JsonProperty("bPompa1")
    private Boolean pumpOneState;

    @JsonProperty("bPompa2")
    private Boolean pumpTwoState;

    @JsonProperty("bAwaria")
    private Boolean accident;

    @JsonIgnore
    private Instant timestamp;
}
