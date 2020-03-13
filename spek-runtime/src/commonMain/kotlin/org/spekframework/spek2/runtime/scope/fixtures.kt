package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.dsl.Fixture

class Fixtures {
    private class GroupFixture(val inheritable: Boolean, val fixture: Fixture) {
        suspend operator fun invoke() {
            fixture()
        }
    }

    private val beforeTestFixtures = mutableListOf<Fixture>()
    private val afterTestFixtures = mutableListOf<Fixture>()
    private val beforeGroupFixtures = mutableListOf<GroupFixture>()
    private val afterGroupFixtures = mutableListOf<GroupFixture>()

    fun beforeEachTest(fixture: Fixture) {
        beforeTestFixtures.add(fixture)
    }

    fun afterEachTest(fixture: Fixture) {
        afterTestFixtures.add(fixture)
    }

    fun beforeGroup(fixture: Fixture) {
        beforeGroupFixtures.add(GroupFixture(false, fixture))
    }

    fun afterGroup(fixture: Fixture) {
        afterGroupFixtures.add(GroupFixture(false, fixture))
    }

    fun beforeEachGroup(fixture: Fixture) {
        beforeGroupFixtures.add(GroupFixture(true, fixture))
    }

    fun afterEachGroup(fixture: Fixture) {
        afterGroupFixtures.add(GroupFixture(true, fixture))
    }


    suspend fun invokeBeforeTestFixtures() {
        beforeTestFixtures.forEach { fixture ->
            fixture()
        }
    }

    suspend fun invokeAfterTestFixtures() {
        afterTestFixtures.forEach { fixture ->
            fixture()
        }
    }

    suspend fun invokeBeforeGroupFixtures(inheritableOnly: Boolean) {
        beforeGroupFixtures.filter { !inheritableOnly || it.inheritable }
            .forEach { fixture ->
                fixture()
            }
    }

    suspend fun invokeAfterGroupFixtures(inheritableOnly: Boolean) {
        afterGroupFixtures.filter { !inheritableOnly || it.inheritable }
            .forEach { fixture ->
                fixture()
            }
    }
}