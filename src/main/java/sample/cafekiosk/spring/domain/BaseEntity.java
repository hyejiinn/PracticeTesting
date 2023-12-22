package sample.cafekiosk.spring.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

// 엔티티가 생성되거나 변경될 때 마다 시간을 찍도록
@Getter
@MappedSuperclass
@EntityListeners(AutoCloseable.class)
public abstract class BaseEntity
{
	@CreatedDate
	private LocalDateTime createdDateTime;
	
	@LastModifiedDate
	private LocalDateTime modifiedDateTime;
	
}
