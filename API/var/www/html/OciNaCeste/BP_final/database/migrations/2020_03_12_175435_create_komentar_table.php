<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateKomentarTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('komentar', function (Blueprint $table) {
            $table->bigIncrements('komentar_id');
            $table->boolean('je_zamestnanec');
            $table->text('komentar');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('komentar', function (Blueprint $table) {

            $table->bigInteger('problem_id')->unsigned();
            $table->bigInteger('pouzivatel_id')->unsigned();


            $table->foreign('problem_id')
                ->references('problem_id')->on('problem')
                ->onDelete('cascade');

            $table->foreign('pouzivatel_id')
                ->references('id')->on('users')
                ->onDelete('cascade');

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('komentar');
    }
}
