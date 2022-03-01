package victor.training.ddd.agile.application.web;

//
//@RestController
//@RequiredArgsConstructor
//public class ReleaseController {
//   private final ReleaseService releaseService;
//
//   @PostMapping("product/{productId}/release/{sprintId}")
//   public ReleaseDto createRelease(@PathVariable long productId, @PathVariable long sprintId) {
//      return releaseService.createRelease(productId, sprintId);
//
//      // excuses for using a separate @RestController that just delegates to a @Service all logic
//      // 1) ResponseEntity manipulation > think of @ExceptionHandler to set http status and body in case of exceptions
//      // 2) FileUploads
//      // 3) security? not sure
//
//   }
//}
