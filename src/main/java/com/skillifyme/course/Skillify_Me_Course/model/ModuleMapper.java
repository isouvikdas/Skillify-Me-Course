package com.skillifyme.course.Skillify_Me_Course.model;

import com.skillifyme.course.Skillify_Me_Course.model.dto.ModuleDTO;
import com.skillifyme.course.Skillify_Me_Course.model.entity.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ModuleMapper {

    @Autowired
    private LessonMapper lessonMapper;

    public ModuleDTO toDto(Module module) {
        ModuleDTO moduleDTO = new ModuleDTO();
        moduleDTO.setModuleId(module.getModuleId());
        moduleDTO.setTitle(module.getTitle());
        moduleDTO.setContent(module.getContent());
        moduleDTO.setLessonIds(module.getLessonIds());
        moduleDTO.setLessonDTOS(module.getLessons().stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList()));
        return moduleDTO;
    }

    public Module toEntity(ModuleDTO moduleDTO) {
        Module module = new Module();
        module.setModuleId(moduleDTO.getModuleId());
        module.setTitle(moduleDTO.getTitle());
        module.setContent(moduleDTO.getContent());
        module.setLessonIds(moduleDTO.getLessonIds());
        module.setLessons(moduleDTO.getLessonDTOS().stream()
                .map(lessonMapper::toEntity)
                .collect(Collectors.toList()));
        return module;
    }
}
