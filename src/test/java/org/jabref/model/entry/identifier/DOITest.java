package org.jabref.model.entry.identifier;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DOITest {

    private static Stream<Arguments> testData() {
        return Stream.of(
                // PlainDoi
                Arguments.of("10.1006/jmbi.1998.2354", new DOI("10.1006/jmbi.1998.2354").asString()),
                Arguments.of("10.231/JIM.0b013e31820bab4c", new DOI("10.231/JIM.0b013e31820bab4c").asString()),
                Arguments.of("10.1002/(SICI)1522-2594(199911)42:5<952::AID-MRM16>3.0.CO;2-S",
                        new DOI("10.1002/(SICI)1522-2594(199911)42:5<952::AID-MRM16>3.0.CO;2-S").asString()),
                Arguments.of("10.1126/sciadv.1500214", new DOI("10.1126/sciadv.1500214").asString()),

                // PlainShortDoi
                Arguments.of("10/gf4gqc", new DOI("10/gf4gqc").asString()),
                Arguments.of("10/1000", new DOI("10/1000").asString()),
                Arguments.of("10/aaaa", new DOI("10/aaaa").asString()),
                Arguments.of("10/adc", new DOI("10/adc").asString()),

                // ignoreLeadingAndTrailingWhitespaces
                Arguments.of("10.1006/jmbi.1998.2354", new DOI("  10.1006/jmbi.1998.2354 ").asString()),

                // ignoreLeadingAndTrailingWhitespacesInShortDoi
                Arguments.of("10/gf4gqc", new DOI("   10/gf4gqc ").asString()),

                // acceptDoiPrefix
                // Doi prefix
                Arguments.of("10.1006/jmbi.1998.2354", new DOI("doi:10.1006/jmbi.1998.2354").asString()),

                // acceptDoiPrefixInShortDoi
                Arguments.of("10/gf4gqc", new DOI("doi:10/gf4gqc").asString()),

                // acceptURNPrefix
                Arguments.of("10.123/456", new DOI("urn:10.123/456").asString()),
                Arguments.of("10.123/456", new DOI("http://doi.org/urn:doi:10.123/456").asString()),
                // : is also allowed as divider, will be replaced by RESOLVER
                Arguments.of("10.123:456ABC/zyz", new DOI("http://doi.org/urn:doi:10.123:456ABC%2Fzyz").asString()),

                // acceptShortcutShortDoi
                Arguments.of("10/d8dn", new DOI("https://doi.org/d8dn").asString()),
                Arguments.of("10/d8dn", new DOI(" https://doi.org/d8dn  ").asString()),
                Arguments.of("10/d8dn", new DOI("doi.org/d8dn").asString()),
                Arguments.of("10/d8dn", new DOI("www.doi.org/d8dn").asString()),
                Arguments.of("10/d8dn", new DOI("  doi.org/d8dn ").asString()),

                // acceptURNPrefixInShortDoi
                Arguments.of("10/gf4gqc", new DOI("urn:10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("doi:10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("urn:doi:10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://doi.org/urn:doi:10/gf4gqc").asString()),
                // : is also allowed as divider, will be replaced by RESOLVER
                Arguments.of("10:gf4gqc", new DOI("http://doi.org/urn:doi:10:gf4gqc").asString()),

                // acceptURLDoi
                // http
                Arguments.of("10.1006/jmbi.1998.2354", new DOI("http://doi.org/10.1006/jmbi.1998.2354").asString()),
                // https
                Arguments.of("10.1006/jmbi.1998.2354", new DOI("https://doi.org/10.1006/jmbi.1998.2354").asString()),
                // https with % divider
                Arguments.of("10.2307/1990888", new DOI("https://dx.doi.org/10.2307%2F1990888").asString()),
                // other domains
                Arguments.of("10.1145/1294928.1294933", new DOI("http://doi.acm.org/10.1145/1294928.1294933").asString()),
                Arguments.of("10.1145/1294928.1294933", new DOI("http://doi.acm.net/10.1145/1294928.1294933").asString()),
                Arguments.of("10.1145/1294928.1294933", new DOI("http://doi.acm.com/10.1145/1294928.1294933").asString()),
                Arguments.of("10.1145/1294928.1294933", new DOI("http://doi.acm.de/10.1145/1294928.1294933").asString()),
                Arguments.of("10.1007/978-3-642-15618-2_19",
                        new DOI("http://dx.doi.org/10.1007/978-3-642-15618-2_19").asString()),
                Arguments.of("10.1007/978-3-642-15618-2_19",
                        new DOI("http://dx.doi.net/10.1007/978-3-642-15618-2_19").asString()),
                Arguments.of("10.1007/978-3-642-15618-2_19",
                        new DOI("http://dx.doi.com/10.1007/978-3-642-15618-2_19").asString()),
                Arguments.of("10.1007/978-3-642-15618-2_19",
                        new DOI("http://dx.doi.de/10.1007/978-3-642-15618-2_19").asString()),
                Arguments.of("10.4108/ICST.COLLABORATECOM2009.8275",
                        new DOI("http://dx.doi.org/10.4108/ICST.COLLABORATECOM2009.8275").asString()),
                Arguments.of("10.1109/MIC.2012.43",
                        new DOI("http://doi.ieeecomputersociety.org/10.1109/MIC.2012.43").asString()),

                // acceptURLShortDoi
                // http
                Arguments.of("10/gf4gqc", new DOI("http://doi.org/10/gf4gqc").asString()),
                // https
                Arguments.of("10/gf4gqc", new DOI("https://doi.org/10/gf4gqc").asString()),
                // https with % divider
                Arguments.of("10/gf4gqc", new DOI("https://dx.doi.org/10%2Fgf4gqc").asString()),
                // other domains
                Arguments.of("10/gf4gqc", new DOI("http://doi.acm.org/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("www.doi.acm.org/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("doi.acm.org/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI(" /10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI(" 10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://doi.acm.net/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://doi.acm.com/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://doi.acm.de/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://dx.doi.org/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://dx.doi.net/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://dx.doi.com/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://dx.doi.de/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://dx.doi.org/10/gf4gqc").asString()),
                Arguments.of("10/gf4gqc", new DOI("http://doi.ieeecomputersociety.org/10/gf4gqc").asString()),

                // parse DOI with whitespace
                Arguments.of("https://doi.org/10.1109/VLHCC.2004.20", DOI.parse("https : / / doi.org / 10 .1109 /V LHCC.20 04.20").get().getURIAsASCIIString()),
                // parse short DOI with whitespace
                Arguments.of("https://doi.org/10/gf4gqc", DOI.parse("https : / / doi.org / 10 / gf4gqc").get().getURIAsASCIIString()),
                // parse DOI with non-ASCII characters and whitespace
                Arguments.of("https://doi.org/10/gf4gqc", DOI.parse("�https : \n  ␛ / / doi.org / \t 10 / \r gf4gqc�␛").get().getURIAsASCIIString()),
                Arguments.of("10/gf4gqc", DOI.parse("�https : \n  ␛ / / doi.org / \t 10 / \r gf4gqc�␛").get().asString()),
                Arguments.of("10/gf4gqc", DOI.parse(" 10 / gf4gqc ").get().asString()),
                Arguments.of("10.3218/3846-0", DOI.parse(" �10.3218\n/384␛6-0�").get().asString()),
                // parse DOI with backslashes
                Arguments.of("10.1007/978-3-030-02671-4_7", DOI.parse("10.1007/978-3-030-02671-4\\_7").get().asString()),
                Arguments.of("10.1007/978-3-030-02671-4_7", DOI.parse("10.1007/\\978-3-03\\0-02671-4\\_7").get().asString()),
                Arguments.of("https://doi.org/10.1007/978-3-030-02671-4_7", DOI.parse("https://doi.org/10.\\\\1007/9\\\\78-3\\\\-030-026\\\\\\71-4_7").get().getURIAsASCIIString()),
                // parse DOI with {}
                Arguments.of("10.1007/978-3-030-02671-4_7", DOI.parse("10.1007/9{}78{-3{-03{0-0}}}26}{71-4}_7").get().asString()),
                // parse DOI with `
                Arguments.of("10.1007/978-3-030-02671-4_7", DOI.parse("10.1007/9`78`-3`-03`0-0``26````71-4}_7").get().asString()),
                // parse DOI with |
                Arguments.of("10.1007/978-3-030-02671-4_7", DOI.parse("10.1007/9||78|-3|-03|0-0|26|71-4|||_7").get().asString()),
                // parse DOI with ~
                Arguments.of("10.1007/978-3-030-02671-4_7", DOI.parse("10.1007/9~~~78~-3~-03~0-0~26~71-4~_7").get().asString()),
                // parse DOI with []
                Arguments.of("10.1007/978-3-030-02671-4_7", DOI.parse("10.1007/][9[][]78-3-03[[]0-02671-4_7").get().asString()),
                // parse DOI with ^
                Arguments.of("10.1007/978-3-030-02671-4_7", DOI.parse("^^^10.10^07/978-3^-0^30-02671-4_7").get().asString()),
                // parse DOI with special characters
                Arguments.of("10.1007/978-3-030-02671-4_7", DOI.parse("10.1^00^^7/9|~^]`7^8-3~[[[]]-0^3]~0-0~26``71-4~||_7").get().asString()),
                // parse already-cleaned DOI
                Arguments.of("10.3218/3846-0", DOI.parse("10.3218/3846-0").get().asString()),

                // correctlyEncodeDOIs
                // See http://www.doi.org/doi_handbook/2_Numbering.html#2.5.2.4
                // % -> (%25)
                Arguments.of("https://doi.org/10.1006/rwei.1999%25.0001",
                        new DOI("https://doi.org/10.1006/rwei.1999%25.0001").getURIAsASCIIString()),
                // " -> (%22)
                Arguments.of("https://doi.org/10.1006/rwei.1999%22.0001",
                        new DOI("https://doi.org/10.1006/rwei.1999%22.0001").getURIAsASCIIString()),
                // # -> (%23)
                Arguments.of("https://doi.org/10.1006/rwei.1999%23.0001",
                        new DOI("https://doi.org/10.1006/rwei.1999%23.0001").getURIAsASCIIString()),
                // SPACE -> (%20)
                Arguments.of("https://doi.org/10.1006/rwei.1999%20.0001",
                        new DOI("https://doi.org/10.1006/rwei.1999%20.0001").getURIAsASCIIString()),
                // ? -> (%3F)
                Arguments.of("https://doi.org/10.1006/rwei.1999%3F.0001",
                        new DOI("https://doi.org/10.1006/rwei.1999%3F.0001").getURIAsASCIIString()),

                // constructCorrectURLForDoi
                // add / to RESOLVER url if missing
                Arguments.of("https://doi.org/10.1006/jmbi.1998.2354",
                        new DOI("10.1006/jmbi.1998.2354").getURIAsASCIIString()),
                Arguments.of("https://doi.org/10.1006/jmbi.1998.2354",
                        new DOI("https://doi.org/10.1006/jmbi.1998.2354").getURIAsASCIIString()),
                Arguments.of("https://doi.org/10.1109/VLHCC.2004.20",
                        new DOI("doi:10.1109/VLHCC.2004.20").getURIAsASCIIString()),

                // constructCorrectURLForShortDoi
                Arguments.of("https://doi.org/10/gf4gqc", new DOI("10/gf4gqc").getURIAsASCIIString()),

                // correctlyDecodeHttpDOIs
                // See http://www.doi.org/doi_handbook/2_Numbering.html#2.5.2.4
                // % -> (%25)
                Arguments.of("10.1006/rwei.1999%.0001", new DOI("http://doi.org/10.1006/rwei.1999%25.0001").asString()),
                // " -> (%22)
                Arguments.of("10.1006/rwei.1999\".0001", new DOI("http://doi.org/10.1006/rwei.1999%22.0001").asString()),
                // # -> (%23)
                Arguments.of("10.1006/rwei.1999#.0001", new DOI("http://doi.org/10.1006/rwei.1999%23.0001").asString()),
                // SPACE -> (%20)
                Arguments.of("10.1006/rwei.1999 .0001", new DOI("http://doi.org/10.1006/rwei.1999%20.0001").asString()),
                // ? -> (%3F)
                Arguments.of("10.1006/rwei.1999?.0001", new DOI("http://doi.org/10.1006/rwei.1999%3F.0001").asString()),
                // <,> -> (%3C, %3E)
                Arguments.of("10.1175/1520-0493(2002)130<1913:EDAWPO>2.0.CO;2", new DOI("https://doi.org/10.1175/1520-0493(2002)130%3C1913:EDAWPO%3E2.0.CO;2").asString()),

                // acceptDoiWithSpecialCharacters
                Arguments.of("10.1175/1520-0493(2002)130<1913:EDAWPO>2.0.CO;2", new DOI("https://doi.org/10.1175/1520-0493(2002)130<1913:EDAWPO>2.0.CO;2").asString()),

                // findDoiInsideArbitraryText
                Arguments.of("10.1006/jmbi.1998.2354",
                        DOI.findInText("other stuff 10.1006/jmbi.1998.2354 end").get().asString()),
                Arguments.of("10.1007/s10549-018-4743-9",
                        DOI.findInText("Breast Cancer Res Treat. 2018 July ; 170(1): 77–87. doi:10.1007/s10549-018-4743-9. ").get().asString()),
                Arguments.of("10.1007/s10549-018-4743-9",
                        DOI.findInText("Breast Cancer Res Treat. 2018 July ; 170(1): 77–87. doi:10.1007/s10549-018-4743-9, ").get().asString()),
                Arguments.of("10.1007/s10549-018-4743-9",
                        DOI.findInText("Breast Cancer Res Treat. 2018 July ; 170(1): 77–87. doi:10.1007/s10549-018-4743-9; something else").get().asString()),
                Arguments.of("10.1007/s10549-018-4743-9.1234",
                        DOI.findInText("bla doi:10.1007/s10549-018-4743-9.1234 with . in doi").get().asString()),

                // findShortDoiInsideArbitraryText
                Arguments.of("10/12ab", DOI.findInText("other stuff doi:10/12ab end").get().asString()),
                Arguments.of("10/12ab", DOI.findInText("other stuff /urn:doi:10/12ab end").get().asString()),
                Arguments.of("10%12ab", DOI.findInText("other stuff doi:10%12ab end").get().asString()),
                Arguments.of("10%12ab", DOI.findInText("other stuff /doi:10%12ab end").get().asString()),
                Arguments.of("10%12ab", DOI.findInText("other stuff /doi:10%12ab, end").get().asString()),
                Arguments.of("10%12ab", DOI.findInText("other stuff /doi:10%12ab. end").get().asString()),
                Arguments.of("10%12ab", DOI.findInText("other stuff /doi:10%12ab; end").get().asString()),
                Arguments.of("10/1234", DOI.findInText("10/B(C)/15 \n" +
                        " \n" +
                        "10:51 \n" +
                        " \n" +
                        " \n" +
                        "doi.org/10/1234 ").get().asString()),

                // findShortcutDoiInsideArbitraryText
                Arguments.of("10/ab123", DOI.findInText("other stuff doi.org/ab123 end").get().asString()),
                Arguments.of("10/76543", DOI.findInText("other stuff www.doi.org/76543 end").get().asString()),
                Arguments.of("10/abcde", DOI.findInText("other stuff https://www.doi.org/abcde end").get().asString()),
                Arguments.of("10/abcde", DOI.findInText("other stuff https://doi.org/abcde end").get().asString()),
                Arguments.of("10.5220/0010404301780189", DOI.findInText("https://www.scitepress.org/Link.aspx?doi=10.5220/0010404301780189").get().asString()),
                Arguments.of("10.5220/0010404301780189", DOI.findInText("10.5220/0010404301780189").get().asString()),

                // findDoiWithSpecialCharactersInText
                Arguments.of("10.1175/1520-0493(2002)130%3C1913:EDAWPO%3E2.0.CO;2",
                        DOI.findInText("https://doi.org/10.1175/1520-0493(2002)130%3C1913:EDAWPO%3E2.0.CO;2").get().asString()),

                // Test with Unicode replacement character
                Arguments.of("10.1006/jmbi.1998.2354", DOI.findInText("other stuff �10.1006/jmbi.1998.2354").get().asString()),
                Arguments.of("10.1006/jmbi.1998.2354", DOI.findInText("other stuff 10.1006/jmbi.1998.2354�").get().asString()),
                Arguments.of("10/gf4gqc", DOI.findInText("other stuff �doi�:10�/gf4����gqc�").get().asString())
        );
    }

    @ParameterizedTest
    @MethodSource("testData")
    void equals(String expected, String input) {
        assertEquals(expected, input);
    }

    @Test
    void equalsWorksFor2017Doi() {
        assertEquals(new DOI("10.1109/cloud.2017.89"), new DOI("10.1109/CLOUD.2017.89"));
    }

    @Test
    void isShortDoiShouldReturnTrueWhenItIsShortDoi() {
        assertTrue(new DOI("10/abcde").isShortDoi());
    }

    @Test
    void noDOIFoundInsideArbitraryText() {
        assertEquals(Optional.empty(), DOI.findInText("text without 28282 a doi"));
        assertEquals(Optional.empty(), DOI.findInText("It's 10:30 o'clock"));
        assertEquals(Optional.empty(), DOI.findInText("...archive number 10/XYZ/123..."));
        assertEquals(Optional.empty(), DOI.findInText("some website poi.org/ab123 end"));
    }

    @Test
    void rejectURLShortDoi() {
        assertThrows(IllegalArgumentException.class, () -> new DOI("http://www.cs.utexas.edu/users/kaufmann/itp-trusted-extensions-aug-2010/summary/summary.pdf"));
        assertThrows(IllegalArgumentException.class, () -> new DOI("http://www.cs.utexas.edu/users/kaufmann/itp-trusted-extensions-aug-20/10/summary/summary.pdf"));
        assertThrows(IllegalArgumentException.class, () -> new DOI("http://www.boi.org/10/2010bingbong"));
    }

    @Test
    void isShortDoiShouldReturnFalseWhenItIsDoi() {
        assertFalse(new DOI("10.1006/jmbi.1998.2354").isShortDoi());
    }

    @Test
    void rejectEmbeddedDoi() {
        assertThrows(IllegalArgumentException.class, () -> new DOI("other stuff 10.1006/jmbi.1998.2354 end"));
    }

    @Test
    void rejectEmbeddedShortDoi() {
        assertThrows(IllegalArgumentException.class, () -> new DOI("other stuff 10/gf4gqc end"));
        assertThrows(IllegalArgumentException.class, () -> new DOI("10/2021/01"));
        assertThrows(IllegalArgumentException.class, () -> new DOI("01/10/2021"));
        assertThrows(IllegalArgumentException.class, () -> new DOI("https://www.abc.de/10/abcd"));
    }

    @Test
    void rejectInvalidDirectoryIndicator() {
        // wrong directory indicator
        assertThrows(IllegalArgumentException.class, () -> new DOI("12.1006/jmbi.1998.2354 end"));
    }

    @Test
    void rejectInvalidDirectoryIndicatorInShortDoi() {
        assertThrows(IllegalArgumentException.class, () -> new DOI("20/abcd"));
    }

    @Test
    void emptyOrUndescoreOnlyReturnsEmpty() {
       assertEquals(Optional.empty(), DOI.parse("_"));
       assertEquals(Optional.empty(), DOI.parse("\t_"));
       assertEquals(Optional.empty(), DOI.parse("___"));
    }

    @Test
    void rejectInvalidDoiUri() {
        assertThrows(IllegalArgumentException.class, () -> new DOI("https://thisisnouri"));
    }

    @Test
    void rejectMissingDivider() {
        // missing divider
        assertThrows(IllegalArgumentException.class, () -> new DOI("10.1006jmbi.1998.2354 end"));
    }

    @Test
    void rejectMissingDividerInShortDoi() {
        assertThrows(IllegalArgumentException.class, () -> new DOI("10gf4gqc end"));
    }

    @Test
    void rejectNullDoiParameter() {
        assertThrows(NullPointerException.class, () -> new DOI(null));
    }
}
