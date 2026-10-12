package ma.xproce.video_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreatorRequest {

	private String name;

	private String email;
}
