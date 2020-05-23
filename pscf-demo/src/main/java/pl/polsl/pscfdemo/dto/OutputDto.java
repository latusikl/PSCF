package pl.polsl.pscfdemo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutputDto {

	@JsonProperty("status")
	String status;
	@JsonProperty("name")
	String name;
}
