package com.cherry.wakeupschedule.model

import java.io.Serializable

/**
 * 课程数据类
 * 表示一门课程的所有信息
 */
data class Course(
    val id: Long = 0,                        // 课程唯一ID
    val name: String,                         // 课程名称
    val teacher: String,                      // 任课教师
    val classroom: String,                     // 上课地点
    val dayOfWeek: Int,                       // 星期几（1-7，周一到周日）
    val startTime: Int,                       // 开始节次
    val endTime: Int,                         // 结束节次
    val startWeek: Int,                       // 开始周
    val endWeek: Int,                         // 结束周
    val weekType: Int = 0,                    // 周类型（0:每周, 1:单周, 2:双周）
    val alarmEnabled: Boolean = true,         // 是否启用闹钟提醒
    val alarmMinutesBefore: Int = 15,          // 提前提醒分钟数
    val color: Int = 0,                       // 课程颜色，0为默认颜色
    val coverImagePath: String = ""            // 课程封面图片路径
) : Serializable