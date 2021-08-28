package com.titxu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.titxu.blog.dao.mapper.CommentMapper;
import com.titxu.blog.dao.pojo.Comment;
import com.titxu.blog.service.CommentService;
import com.titxu.blog.service.SysUserService;
import com.titxu.blog.utils.UserThreadLocal;
import com.titxu.blog.vo.CommentVo;
import com.titxu.blog.vo.SysUserVo;
import com.titxu.blog.vo.UserVo;
import com.titxu.blog.vo.params.CommentParam;
import com.titxu.blog.utils.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 评论
     *
     * @param id
     * @return
     */
    @Override
    public Result commentsByArticleId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId, id);
        queryWrapper.eq(Comment::getLevel, 1);
        final List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    @Override
    public Result commentsCreateArticle(CommentParam commentParam) {
        System.out.println(commentParam.toString());
        SysUserVo sysUserVo = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUserVo.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if (parent == null || parent == 0) {
            comment.setLevel(1);
        } else {
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success("ok");
    }

    /**
     * 转list
     * @param comments
     * @return
     */
    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    /**
     * 复制 pojo到vo对象
     *
     * @param comment
     * @return
     */
    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        // 作者信息
        final Long authorId = comment.getAuthorId();
        final UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        // 子评论
        final Integer level = comment.getLevel();
        if (level == 1) {
            final Long id = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParenId(id);
            commentVo.setChildrens(commentVoList);

        }
        // to user  给那个用户评论
        if (level > 1) {
            final Long toUid = comment.getToUid();
            final UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);


        }
        return commentVo;

    }

    private List<CommentVo> findCommentsByParenId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        return copyList(commentMapper.selectList(queryWrapper));
    }
}
