package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.dsl.Fixture

class Fixtures {
    private class GroupFixture(val inheritable: Boolean, val fixture: Fixture) {
        operator fun invoke() {
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


    fun invokeBeforeTestFixtures() {
        beforeTestFixtures.forEach(Fixture::invoke)
    }

    fun invokeAfterTestFixtures() {
        afterTestFixtures.forEach(Fixture::invoke)
    }

    fun invokeBeforeGroupFixtures(inheritableOnly: Boolean) {
        beforeGroupFixtures.filter { !inheritableOnly || it.inheritable }
            .forEach(GroupFixture::invoke)
    }

    fun invokeAfterGroupFixtures(inheritableOnly: Boolean) {
        afterGroupFixtures.filter { !inheritableOnly || it.inheritable }
            .forEach(GroupFixture::invoke)
    }
}