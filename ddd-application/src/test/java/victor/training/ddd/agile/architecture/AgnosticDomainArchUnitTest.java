package victor.training.ddd.agile.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;
import org.junit.Test;

public class AgnosticDomainArchUnitTest {
   @Test
   public void dependencyInversionTest() {
      JavaClasses classes = new ClassFileImporter().importPackages("victor.training");

      ClassesShouldConjunction domainDoesnDependOnInfra = ArchRuleDefinition.noClasses()
          .that().resideInAPackage("..domain..")
          .should()
          .dependOnClassesThat().resideInAPackage("..application..");

      domainDoesnDependOnInfra.check(classes);
   }
}
