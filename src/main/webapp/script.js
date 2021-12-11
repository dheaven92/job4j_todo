$(document).ready(function () {

    const URL = `${window.location.origin}/todo`;

    const setItemsToggable = function () {
        $('.todo-list .todo-item input').click(function () {
            const id = $(this).attr('data-id');
            const description = $(this).attr('data-description');
            const checked = $(this).is(':checked');

            $.ajax({
                type: 'POST',
                url: `${URL}/items`,
                data: JSON.stringify({
                    id,
                    description,
                    done: checked
                }),
                dataType: 'json'
            }).done(item => {
                if (item.done) {
                    $(this).parent().parent().parent().toggleClass('complete');
                } else {
                    $(this).parent().parent().parent().toggleClass('complete');
                }
            }).fail(err => {
                console.log(err);
            });
        });
    };

    const todo = function () {

        $.ajax({
            type: 'GET',
            url: `${URL}/items`,
            dataType: 'json'
        }).done(items => {
            for (const item of items) {
                $('.todo-list').append(
                    `
                       <div class="todo-item ${item.done ? 'complete' : ''}">
                           <div class="checker">
                                <span>
                                    <input 
                                        type="checkbox" ${item.done ? 'checked' : ''} 
                                        data-id=${item.id}
                                        data-description="${item.description}"
                                    >
                                </span>
                            </div>
                            <span>${item.description}</span>
                        </div>
                        <a href="javascript:void(0);" class="float-right remove-todo-item">
                            <i class="icon-close"></i>
                        </a>
                       </div>
                   `);
            }
            setItemsToggable();
        }).fail(function (err) {
            console.log(err);
        });

        $('.todo-nav .all-task').click(function () {
            $('.todo-list').removeClass('only-active');
            $('.todo-list').removeClass('only-complete');
            $('.todo-nav li.active').removeClass('active');
            $(this).addClass('active');
        });

        $('.todo-nav .active-task').click(function () {
            $('.todo-list').removeClass('only-complete');
            $('.todo-list').addClass('only-active');
            $('.todo-nav li.active').removeClass('active');
            $(this).addClass('active');
        });

        $('.todo-nav .completed-task').click(function () {
            $('.todo-list').removeClass('only-active');
            $('.todo-list').addClass('only-complete');
            $('.todo-nav li.active').removeClass('active');
            $(this).addClass('active');
        });
    };

    todo();

    $(".add-task").keypress(function (e) {
        if ((e.which == 13) && (!$(this).val().length == 0)) {
            const description = $(this).val();
            $.ajax({
                type: 'POST',
                url: `${URL}/items`,
                data: JSON.stringify({
                    description
                }),
                dataType: 'json'
            }).done(item => {
                $('.todo-list').prepend(
                    `
                       <div class="todo-item">
                           <div class="checker">
                                <span>
                                    <input 
                                        type="checkbox" 
                                        data-id=${item.id} 
                                        data-description="${item.description}"
                                    >
                                </span>
                            </div>
                            <span>${item.description}</span>
                        </div>
                        <a href="javascript:void(0);" class="float-right remove-todo-item">
                            <i class="icon-close"></i>
                        </a>
                       </div>
                   `);
                $(this).val('');
                setItemsToggable();
            }).fail(err => {
                console.log(err);
            });
        } else if (e.which == 13) {
            alert('Введите название задания');
        }
        $(document).on('.todo-list .todo-item.added input').click(function () {
            if ($(this).is(':checked')) {
                $(this).parent().parent().parent().toggleClass('complete');
            } else {
                $(this).parent().parent().parent().toggleClass('complete');
            }
        });
        $('.todo-list .todo-item.added .remove-todo-item').click(function () {
            $(this).parent().remove();
        });
    });
});