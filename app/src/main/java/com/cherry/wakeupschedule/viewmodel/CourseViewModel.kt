package com.cherry.wakeupschedule.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cherry.wakeupschedule.App
import com.cherry.wakeupschedule.model.Course
import com.cherry.wakeupschedule.service.AlarmService
import com.cherry.wakeupschedule.service.CourseDataManager
import com.cherry.wakeupschedule.service.SettingsManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

/**
 * 课程ViewModel
 * 管理课程数据的增删改查和闹钟设置
 */
class CourseViewModel(application: Application) : AndroidViewModel(application) {

    // 当前显示的课程列表
    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses

    private val courseDataManager = CourseDataManager.getInstance(application)
    private val settingsManager = SettingsManager(application)
    private val alarmService = App.instance.alarmService
    @Volatile
    private var activeWeek: Int = calculateCurrentWeek()

    init {
        // 监听课程数据变化
        viewModelScope.launch {
            courseDataManager.coursesFlow.collect { allCourses ->
                val week = activeWeek
                val coursesForWeek = allCourses.filter { course ->
                    week in course.startWeek..course.endWeek
                }
                _courses.postValue(coursesForWeek)
            }
        }
    }

    // 获取所有课程
    fun getAllCourses(): Flow<List<Course>> {
        return flow { emit(courseDataManager.getAllCourses()) }
    }

    // 按星期获取课程
    fun getCoursesByDay(dayOfWeek: Int): Flow<List<Course>> {
        return flow {
            emit(courseDataManager.getAllCourses().filter { it.dayOfWeek == dayOfWeek })
        }
    }

    // 加载指定周的课程
    fun loadCoursesForWeek(week: Int) {
        activeWeek = week
        viewModelScope.launch {
            val coursesForWeek = courseDataManager.getCoursesForWeek(week)
            _courses.postValue(coursesForWeek)
        }
    }

    // 添加课程
    fun addCourse(course: Course) {
        viewModelScope.launch {
            courseDataManager.addCourse(course)
            alarmService?.setCourseAlarm(course)
        }
    }

    // 批量添加课程
    fun addCourses(courses: List<Course>) {
        viewModelScope.launch {
            courseDataManager.addCourses(courses)
            courses.forEach { course ->
                alarmService?.setCourseAlarm(course)
            }
        }
    }

    // 更新课程
    fun updateCourse(course: Course) {
        viewModelScope.launch {
            courseDataManager.updateCourse(course)
            alarmService?.setCourseAlarm(course)
        }
    }

    // 删除课程
    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            courseDataManager.deleteCourse(course)
            alarmService?.cancelCourseAlarm(course)
        }
    }

    // 清空所有课程
    fun clearAllCourses() {
        viewModelScope.launch {
            courseDataManager.clearAllCourses()
        }
    }

    // 刷新课程
    fun refreshCourses() {
        viewModelScope.launch {
            courseDataManager.refreshCourses()
        }
    }

    // 计算当前周数
    private fun calculateCurrentWeek(): Int {
        val semesterStartDate = settingsManager.getSemesterStartDate()
        if (semesterStartDate <= 0L) {
            return settingsManager.getDefaultWeek().coerceIn(1, 20)
        }
        val diffMillis = System.currentTimeMillis() - semesterStartDate
        val diffDays = (diffMillis / (24 * 60 * 60 * 1000L)).toInt()
        return ((diffDays / 7) + 1).coerceIn(1, 20)
    }
}
