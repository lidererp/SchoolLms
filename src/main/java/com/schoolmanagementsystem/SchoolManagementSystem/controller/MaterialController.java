package com.schoolmanagementsystem.SchoolManagementSystem.controller;

import com.schoolmanagementsystem.SchoolManagementSystem.entity.Material;
import com.schoolmanagementsystem.SchoolManagementSystem.service.MaterialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materials")
public class MaterialController {


    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }


    @PostMapping("/upload-url")
    public ResponseEntity<Material> uploadMaterialByUrl(
            @RequestParam String fileUrl,
            @RequestParam(required = false) Long standardId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam Long academicYearId,
            @RequestParam(required = false) String description) {

        Material material = materialService.uploadMaterialByUrl(
                fileUrl, standardId, sectionId, academicYearId, description
        );
        return ResponseEntity.ok(material);
    }


    @GetMapping
    public ResponseEntity<List<Material>> getMaterials(
            @RequestParam(required = false) Long standardId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam Long academicYearId) {

        List<Material> materials = materialService.getMaterials(standardId, sectionId, academicYearId);
        return ResponseEntity.ok(materials);
    }


    @GetMapping("/all")
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = materialService.getAllMaterials();
        return ResponseEntity.ok(materials);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(
            @PathVariable Long id,
            @RequestParam(required = false) String fileUrl,
            @RequestParam(required = false) Long standardId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) Long academicYearId,
            @RequestParam(required = false) String description) {

        Material updated = materialService.updateMaterialByUrl(
                id, fileUrl, standardId, sectionId, academicYearId, description);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok("Material deleted successfully");
    }


}
