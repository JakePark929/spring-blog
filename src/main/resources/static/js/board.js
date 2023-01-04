let index = {
    // let _this = this; function 사용시 this 는 window 객체가 됨
    init: function () {
        $("#btn-board-save").on("click", () => { // function(){}, ()->{} this 를 바인딩하기 위해서!
            this.save();
        });
        $("#btn-board-update").on("click", () => { // function(){}, ()->{} this 를 바인딩하기 위해서!
            this.update();
        });
        $("#btn-board-delete").on("click", () => { // function(){}, ()->{} this 를 바인딩하기 위해서!
            this.deleteById();
        });
        $("#btn-reply-save").on("click", () => { // function(){}, ()->{} this 를 바인딩하기 위해서!
            this.replySave();
        });
    },

    save: function () {
        // alert('user 의 save 함수 호출됨');
        let data = {
            title: $("#title").val(),
            content: $("#content").val(),
        };

        $.ajax({
            // 회원가입 수행 요청 (100초 가정)
            type: "POST",
            url: "/api/board",
            data: JSON.stringify(data), // http body 데이터
            contentType: "application/json; charset=utf-8", // body 데이터가 어떤타입인지(MIME)
            dataType: "json" // 요청을 서버로해서 응답이 왔을 때, 기본적으로 모든 것이 String (생긴게 json 이라면) => javascript 오브젝트로 변경해줌
        }).done(function (response) {
            alert("글쓰기가 완료되었습니다.");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    update: function () {
        // alert('user 의 save 함수 호출됨');
        let id=$("#id").val();

        let data = {
            title: $("#title").val(),
            content: $("#content").val(),
        };

        $.ajax({
            // 회원가입 수행 요청 (100초 가정)
            type: "PUT",
            url: "/api/board/"+id,
            data: JSON.stringify(data), // http body 데이터
            contentType: "application/json; charset=utf-8", // body 데이터가 어떤타입인지(MIME)
            dataType: "json" // 요청을 서버로해서 응답이 왔을 때, 기본적으로 모든 것이 String (생긴게 json 이라면) => javascript 오브젝트로 변경해줌
        }).done(function (response) {
            alert("글 수정이 완료되었습니다.");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    deleteById: function () {
        // alert('user 의 save 함수 호출됨');
        let id = $("#id").text();
        $.ajax({
            // 회원가입 수행 요청 (100초 가정)
            type: "DELETE",
            url: "/api/board/" + id,
            dataType: "json" // 요청을 서버로해서 응답이 왔을 때, 기본적으로 모든 것이 String (생긴게 json 이라면) => javascript 오브젝트로 변경해줌
        }).done(function (response) {
            alert("삭제가 완료되었습니다.");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    replySave: function () {
        // alert('user 의 save 함수 호출됨');
        // let boardId = $("#boardId").val();
        let data = {
            username: $("#username").val(),
            boardId: $("#boardId").val(),
            content: $("#reply-content").val(),
        };

        $.ajax({
            // 회원가입 수행 요청 (100초 가정)
            type: "POST",
            url: `/api/board/${data.boardId}/reply`,
            data: JSON.stringify(data), // http body 데이터
            contentType: "application/json; charset=utf-8", // body 데이터가 어떤타입인지(MIME)
            dataType: "json" // 요청을 서버로해서 응답이 왔을 때, 기본적으로 모든 것이 String (생긴게 json 이라면) => javascript 오브젝트로 변경해줌
        }).done(function (response) {
            alert("댓글 작성이 완료되었습니다.");
            location.href = `/board/${data.boardId}`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    replyDelete: function (boardId, replyId) {
        $.ajax({
            // 회원가입 수행 요청 (100초 가정)
            type: "DELETE",
            url: `/api/board/${boardId}/reply/${replyId}`,
            dataType: "json" // 요청을 서버로해서 응답이 왔을 때, 기본적으로 모든 것이 String (생긴게 json 이라면) => javascript 오브젝트로 변경해줌
        }).done(function (response) {
            alert("댓글 삭제 성공");
            location.href = `/board/${boardId}`;
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
}

index.init();