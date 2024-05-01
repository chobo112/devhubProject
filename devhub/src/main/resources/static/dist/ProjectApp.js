$(document).ready(function() {
    // 별점 변경 이벤트 핸들러
    $('[id^=star-]').each(function() {
        var id = $(this).attr('id');
        var index = id.split('-')[1];
        var inputId = '#star_input-' + index;
        var $input = $(inputId);
        var initialRating = $input.val() ? parseInt($input.val(), 10) : 0;

        $(this).starrr({
            rating: initialRating,
            change: function(e, value) {
                $input.val(value);
                var scScore = value;
                var boardId = $(this).closest('.max-w-sm').find('.board-id').val();

                // AJAX 요청을 통해 서버에 sc_score 업데이트
                $.ajax({
                    type: 'POST',
                    url: '/update-score',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        boardId: boardId,
                        scScore: value
                    }),
                    success: function(score) {
                        console.log('Score updated successfully');
                        $('#board-' + index + ' .sc-score').text(score.sc_score);
                    },
                    error: function(xhr, status, error) {
                        console.error('Error updating score:', error);
                    }
                });
            }
        });
    });

    // 댓글 수정 이벤트 핸들러
    $(document).on('click', '.edit-icon', function() {
        var $commentContent = $(this).closest('.max-w-sm').find('#comment-content');
        var currentComment = $commentContent.text().trim(); 
        var boardId = $(this).closest('.max-w-sm').find('.board-id').val();
        $commentContent.html(`
            <div class="relative z-0 w-full">
                <textarea id="comment-edit" class="block w-full px-0 py-2 text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none dark:text-white dark:border-gray-600 dark:focus:border-blue-500 focus:outline-none focus:ring-0 focus:border-blue-600 peer">${currentComment}</textarea>
            </div>
            <button type="submit" id="save-comment" class="inline-flex items-center py-2.5 px-4 text-xs font-medium text-center text-white bg-gray-700 rounded-lg focus:ring-4 focus:ring-blue-200 dark:focus:ring-blue-900 hover:bg-blue-800">
                저장
            </button>
        `);
    });

    // 저장 버튼 클릭 이벤트 핸들러
    $(document).on('click', '#save-comment', function() {
        var $commentContent = $(this).closest('.max-w-sm').find('#comment-content');
        var updatedComment = $('#comment-edit').val();
        var boardId = $(this).closest('.max-w-sm').find('.board-id').val();

        // AJAX 요청을 통해 서버에 수정된 내용 전송
        $.ajax({
            type: 'POST',
            url: '/update-comment',
            contentType: 'application/json',
            data: JSON.stringify({
                board_id: boardId,
                sc_cmt: updatedComment
            }),
            success: function(response) {
                $commentContent.html('<span id="current-comment">' + updatedComment + '</span>');
            },
            error: function(xhr, status, error) {
                console.error('Error updating comment:', error);
            }
        });
    });
});
