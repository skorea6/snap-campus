package com.example.snapcampus.service;


import com.example.snapcampus.common.dto.PageWrapper;
import com.example.snapcampus.dto.request.comment.CommentAddDtoRequest;
import com.example.snapcampus.dto.request.comment.CommentDeleteDtoRequest;
import com.example.snapcampus.dto.request.comment.CommentListDtoRequest;
import com.example.snapcampus.dto.request.comment.CommentUpdateDtoRequest;
import com.example.snapcampus.dto.response.comment.CommentDtoResponse;
import com.example.snapcampus.entity.Comment;
import com.example.snapcampus.entity.Member;
import com.example.snapcampus.entity.Post;
import com.example.snapcampus.repository.CommentRepository;
import com.example.snapcampus.repository.MemberRepository;
import com.example.snapcampus.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PageWrapper<CommentDtoResponse> list(CommentListDtoRequest commentListDtoRequest){
        Post post = postFindById(commentListDtoRequest.getPostId());

        Pageable pageable = PageRequest.of(
                (int) Math.max(0, commentListDtoRequest.getPage() - 1),
                (int) Math.max(0, commentListDtoRequest.getPageSize()),
                "desc".equals(commentListDtoRequest.getSortDirection()) ? Sort.by(Sort.Order.desc("createdAt")) : Sort.by(Sort.Order.asc("createdAt"))
        );

        Page<Comment> comments = commentRepository.findAllByPostAndIsDeleted(pageable, post, false);
        return PageWrapper.fromPage(comments.map(Comment::toDto), 5);
    }


    public String add(String memberUserId, CommentAddDtoRequest commentAddDtoRequest) {
        Member member = memberFindByUserId(memberUserId);
        Post post = postFindById(commentAddDtoRequest.getPostId());

        commentRepository.save(new Comment(member, post, commentAddDtoRequest.getContent()));
        return "댓글이 작성되었습니다.";
    }


    public String update(String memberUserId, CommentUpdateDtoRequest commentUpdateDtoRequest) {
        Comment comment = commentFindByIdAndNotDeleted(commentUpdateDtoRequest.getId());
        if(!comment.getMember().getUserId().equals(memberUserId)){
            throw new IllegalArgumentException("본인의 댓글만 수정이 가능합니다.");
        }

        comment.update(commentUpdateDtoRequest.getContent());
        return "댓글이 수정되었습니다.";
    }


    public String delete(String memberUserId, Boolean isAdmin, CommentDeleteDtoRequest commentDeleteDtoRequest) {
        Comment comment = commentFindByIdAndNotDeleted(commentDeleteDtoRequest.getId());
        if(!isAdmin && !comment.getMember().getUserId().equals(memberUserId)){
            throw new IllegalArgumentException("본인의 댓글만 삭제가 가능합니다.");
        }

        String deletedMessage = isAdmin ? "운영자에 의해 삭제된 댓글입니다." : "본인에 의해 삭제된 댓글입니다.";
        comment.delete(deletedMessage);

        return "댓글이 삭제되었습니다.";
    }


    private Member memberFindByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    private Post postFindById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 post입니다.")
        );
    }

    private Comment commentFindByIdAndNotDeleted(Long commentId) {
        return commentRepository.findByIdAndIsDeleted(commentId, false).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 댓글입니다.")
        );
    }
}
