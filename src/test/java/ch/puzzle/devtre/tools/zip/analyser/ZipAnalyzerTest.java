package ch.puzzle.devtre.tools.zip.analyser;


import ch.puzzle.devtre.test.setup.TestResourcesResolver;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

public class ZipAnalyzerTest {

    private final ZipAnalyzer zipAnalyzer = new ZipAnalyzer();

    @Test
    @SneakyThrows
    public void test() {
        // given
        val siardArchive = TestResourcesResolver.loadResource("siard-projects/corrupt/simple-teams-example_postgres13_2-2.siard");
        //val siardArchive = TestResourcesResolver.loadResource(SiardProjectExamples.SIMPLE_TEAMS_EXAMPLE_ORACLE18_2_2);

        // when
        zipAnalyzer.analyze(siardArchive);

        // then
    }

}