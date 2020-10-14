package ru.netology

import org.junit.Test

import org.junit.Assert.*
import ru.netology.exceptions.*

class MainKtTest {

    @Test
    fun main_add() {
        val wall = WallService
        val post = Post()

        wall.clear()
        wall.add(post)
        wall.add(post.copy(date = 1))
        val lastPost = wall.add(post.copy(date = 2))

        assertNotEquals(0, lastPost.id)
    }

    @Test
    fun main_update_existing() {
        val wall = WallService
        val post = Post()

        wall.clear()
        wall.add(post)
        wall.add(post.copy(date = 1))
        wall.add(post.copy(date = 2))

        val updated = wall.update(post.copy(id = 1, text = "new text"))

        assertTrue(updated)
    }

    @Test
    fun main_update_notExisting() {
        val wall = WallService
        val post = Post()

        wall.clear()
        wall.add(post)
        wall.add(post.copy(date = 1))
        wall.add(post.copy(date = 2))

        val updated = wall.update(post.copy(id = 3, text = "new text"))

        assertFalse(updated)
    }

    @Test
    fun main_equalPosts() {
        val post = Post()
        val clonePost = post.copy()

        assertEquals(post, clonePost)
    }

    @Test
    fun main_hashPost() {
        val hash = Post().hashCode()

        assertNotEquals(0, hash)
    }

    @Test(expected = PostNotFoundException::class)
    fun main_createCommentThrowsException() {
        WallService.createComment(Comment(), Int.MIN_VALUE)
    }

    @Test
    fun main_createComment() {
        val wall = WallService

        wall.clear()
        val post = wall.add(Post())

        val result = wall.createComment(Comment(), post.id)

        assertNotNull(result)
    }

    @Test
    fun main_report_success() {
        val wall = WallService

        wall.clear()
        val post = wall.add(Post())
        val comment = Comment()

        wall.createComment(comment, post.id)
        wall.createComment(comment, post.id)
        val lastComment = wall.createComment(comment, post.id)

        val result = if (lastComment != null) {
            wall.reportComment(
                Report(
                    ownerId = lastComment.fromId,
                    commentId = lastComment.id,
                    reportReason = ReportReason.SPAM
                )
            )
        } else false

        assertTrue(result)
    }

    @Test(expected = CommentNotFoundException::class)
    fun main_report_commentNotFoundException() {
        val wall = WallService

        wall.clear()
        val post = wall.add(Post())
        val comment = wall.createComment(Comment(), post.id)

        if (comment != null) {
            wall.reportComment(
                Report(
                    ownerId = comment.fromId,
                    commentId = comment.id + 1,
                    reportReason = ReportReason.ABUSE
                )
            )
        }
    }

    @Test(expected = CommentOwnerNotFoundException::class)
    fun main_report_commentOwnerNotFoundException() {
        val wall = WallService

        wall.clear()
        val post = wall.add(Post())
        val comment = wall.createComment(Comment(), post.id)

        if (comment != null) {
            wall.reportComment(
                Report(
                    ownerId = comment.fromId + 1,
                    commentId = comment.id,
                    reportReason = ReportReason.VIOLENCE
                )
            )
        }
    }
}