$(document).ready(function () {
    setNavigation();
    loadItems();
    setFormSubmittable();
});

const setFormSubmittable = () => {
    $(".add-task").keypress(function (e) {
        if ((e.which == 13) && (!$(this).val().length == 0)) {
            const description = $(this).val();

            $.ajax({
                type: 'POST',
                url: `${window.location.origin}/todo/items`,
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
                                    <input type="checkbox" data-id=${item.id}>
                                </span>
                            </div>
                            <span>${item.description}</span>
                            <span class="todo-item-author">автор: ${item.user.name}</span>
                        </div>
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
    });
}

const loadItems = () => {
    $.ajax({
        type: 'GET',
        url: `${window.location.origin}/todo/items`,
        dataType: 'json'
    }).done(items => {
        for (const item of items) {
            $('.todo-list').append(
                `
                       <div class="todo-item ${item.done ? 'complete' : ''}">
                           <div class="checker">
                                <span>
                                    <input type="checkbox" ${item.done ? 'checked' : ''} data-id=${item.id}>
                                </span>
                            </div>
                            <span>${item.description}</span>
                            <span class="todo-item-author">автор: ${item.user.name}</span>
                        </div>
                       </div>
                   `);
        }
        setItemsToggable();
    }).fail(function (err) {
        console.log(err);
    });
}

const setItemsToggable = () => {
    $('.todo-list .todo-item input').click(function () {
        const id = $(this).attr('data-id');

        $.ajax({
            type: 'PUT',
            url: `${window.location.origin}/todo/items`,
            data: JSON.stringify({
                id
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

const setNavigation = () => {
    const list = $('.todo-list');

    $('.todo-nav .all-task').click(function () {
        list.removeClass('only-active');
        list.removeClass('only-complete');
        $('.todo-nav li.active').removeClass('active');
        $(this).addClass('active');
    });

    $('.todo-nav .active-task').click(function () {
        list.removeClass('only-complete');
        list.addClass('only-active');
        $('.todo-nav li.active').removeClass('active');
        $(this).addClass('active');
    });

    $('.todo-nav .completed-task').click(function () {
        list.removeClass('only-active');
        list.addClass('only-complete');
        $('.todo-nav li.active').removeClass('active');
        $(this).addClass('active');
    });
}

const logout = () => {
    $.ajax({
        type: 'GET',
        url: `${window.location.origin}/todo/logout`
    }).done(data => {
        window.location.href = `${window.location.origin}/todo/login`;
    }).fail(err => {
        console.log(err);
    });
}