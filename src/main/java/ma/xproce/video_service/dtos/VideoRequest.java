package ma.xproce.video_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoRequest {

	private String name;

	private String url;

	private String description;

	private String datePublication;

	private CreatorRequest creator;
}
