/*
 * Copyright (c) 2020 Proton Technologies AG
 * This file is part of Proton Technologies AG and ProtonCore.
 *
 * ProtonCore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ProtonCore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ProtonCore.  If not, see <https://www.gnu.org/licenses/>.
 */

import studio.forface.easygradle.dsl.*
import studio.forface.easygradle.dsl.android.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android(
    minSdk = 23,
    version = Version(0, 3, 2),
    useDataBinding = true
)

dependencies {

    implementation(

        project(Module.kotlinUtil),
        project(Module.presentation),
        project(Module.network),
        project(Module.domain),
        project(Module.data),

        // Features
        project(Module.account),
        project(Module.accountManager),
        project(Module.accountManagerDagger),
        project(Module.auth),
        project(Module.crypto),
        project(Module.data),
        project(Module.domain),
        project(Module.gopenpgp),
        project(Module.humanVerification),
        project(Module.key),
        project(Module.user),
        project(Module.mailMessage),
        project(Module.payment),
        project(Module.countries),

        `kotlin-jdk7`,
        `coroutines-android`,

        // Android
        `activity`,
        `appcompat`,
        `constraint-layout`,
        `fragment`,
        `hilt-android`,
        `lifecycle-viewModel`,
        `hilt-androidx-annotations`,
        `hilt-androidx-viewModel`,
        `material`,
        `viewStateStore`,
        `android-work-runtime`,

        // Other
        `retrofit`,
        `timber`
    )

    compileOnly(
        `android-annotation`,
        `assistedInject-annotations-dagger`
    )

    kapt(
        `assistedInject-processor-dagger`,
        `hilt-android-compiler`,
        `hilt-androidx-compiler`
    )

    // Test
    testImplementation(project(Module.androidTest))
    androidTestImplementation(project(Module.androidInstrumentedTest))

    // Lint - off temporary
//    lintChecks(project(Module.lint))
}
