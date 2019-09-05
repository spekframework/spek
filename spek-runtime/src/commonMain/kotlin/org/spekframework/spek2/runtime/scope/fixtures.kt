package org.spekframework.spek2.runtime.scope

import org.spekframework.spek2.dsl.Fixture

interface FixtureContainer {
    fun beforeEachTest(fixture: Fixture)
    fun afterEachTest(fixture: Fixture)
    fun beforeEachGroup(fixture: Fixture)
    fun afterEachGroup(fixture: Fixture)
    fun beforeGroup(fixture: Fixture)
    fun afterGroup(fixture: Fixture)


    fun invokeBeforeTestFixtures()
    fun invokeAfterTestFixtures()
    fun invokeBeforeGroupFixtures(inheritableOnly: Boolean)
    fun invokeAfterGroupFixtures(inheritableOnly: Boolean)
}

class Fixtures: FixtureContainer {
    private class GroupFixture(val inheritable: Boolean, val fixture: Fixture) {
        operator fun invoke() {
            fixture()
        }
    }

    private val beforeTestFixtures = mutableListOf<Fixture>()
    private val afterTestFixtures = mutableListOf<Fixture>()
    private val beforeGroupFixtures = mutableListOf<GroupFixture>()
    private val afterGroupFixtures = mutableListOf<GroupFixture>()

    override fun beforeEachTest(fixture: Fixture) {
        beforeTestFixtures.add(fixture)
    }

    override fun afterEachTest(fixture: Fixture) {
        afterTestFixtures.add(fixture)
    }

    override fun beforeGroup(fixture: Fixture) {
        beforeGroupFixtures.add(GroupFixture(false, fixture))
    }

    override fun afterGroup(fixture: Fixture) {
        afterGroupFixtures.add(GroupFixture(false, fixture))
    }

    override fun beforeEachGroup(fixture: Fixture) {
        beforeGroupFixtures.add(GroupFixture(true, fixture))
    }

    override fun afterEachGroup(fixture: Fixture) {
        afterGroupFixtures.add(GroupFixture(true, fixture))
    }


    override fun invokeBeforeTestFixtures() {
        beforeTestFixtures.forEach(Fixture::invoke)
    }

    override fun invokeAfterTestFixtures() {
        afterTestFixtures.forEach(Fixture::invoke)
    }

    override fun invokeBeforeGroupFixtures(inheritableOnly: Boolean) {
        beforeGroupFixtures.filter { !inheritableOnly || it.inheritable }
            .forEach(GroupFixture::invoke)
    }

    override fun invokeAfterGroupFixtures(inheritableOnly: Boolean) {
        afterGroupFixtures.filter { !inheritableOnly || it.inheritable }
            .forEach(GroupFixture::invoke)
    }
}