package org.art.web.warrior.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.commons.CommonConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.art.web.warrior.commons.CommonConstants.*;

@Slf4j
class CompilationServiceHelper {

    private CompilationServiceHelper() {
    }

    static List<String> buildCompilerOptions() {
        List<String> compOptions = new ArrayList<>();
        String osName = System.getProperty(OS_NAME_SYS_PROP_NAME);
        if (isLinux(osName)) {
            compOptions.addAll(buildClassPathOptions());
        } else if (isWindows(osName)) {
            //TODO: Handle Windows deployment
        } else {
            log.warn("Cannot build compilation options! Unsupported OS: {}", osName);
        }
        log.info("Compilation options: {}", compOptions);
        return compOptions;
    }

    private static List<String> buildClassPathOptions() {
        List<String> cpOptions = new ArrayList<>();
        String classPathDir = System.getProperty(USER_DIR_SYS_PROP_NAME) + "/bin";
        String cp = getClassPathDependenciesAsString(classPathDir);
        if (StringUtils.isNotBlank(cp)) {
            cpOptions.addAll(Arrays.asList("-classpath", cp));
        }
        return cpOptions;
    }

    private static String getClassPathDependenciesAsString(String targetDir) {
        String dependencies = StringUtils.EMPTY;
        File dir = new File(targetDir);
        if (dir.exists()) {
            File[] listFiles = dir.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                dependencies = Stream.of(listFiles)
                    .filter(File::isFile)
                    .map(File::getName)
                    .filter(fName -> StringUtils.endsWith(fName, ".jar"))
                    .map(fName -> targetDir + SLASH_CH + fName)
                    .collect(joining(CommonConstants.COMMA));
            }
        }
        return dependencies;
    }

    private static boolean isLinux(String osName) {
        return StringUtils.contains(osName, LINUX_OS_PROP_VALUE);
    }

    private static boolean isWindows(String osName) {
        return StringUtils.contains(osName, WINDOWS_OS_PROP_VALUE);
    }
}
