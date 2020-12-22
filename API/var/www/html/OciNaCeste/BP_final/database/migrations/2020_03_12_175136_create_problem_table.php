<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateProblemTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('problem', function (Blueprint $table) {
            $table->bigIncrements('problem_id');
            $table->text('poloha');
            $table->text('popis_problemu');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('problem', function (Blueprint $table) {

            $table->bigInteger('priorita_id')->unsigned();
            $table->bigInteger('cesta_id')->unsigned();
            $table->bigInteger('pouzivatel_id')->unsigned();
            $table->bigInteger('kategoria_problemu_id')->unsigned();
            $table->bigInteger('stav_problemu_id')->unsigned();



            $table->foreign('priorita_id')
                ->references('priorita_id')->on('priorita')
                ->onDelete('cascade');

            $table->foreign('cesta_id')
                ->references('cesta_id')->on('cesta')
                ->onDelete('cascade');

            $table->foreign('pouzivatel_id')
                ->references('id')->on('users')
                ->onDelete('cascade');

            $table->foreign('kategoria_problemu_id')
                ->references('kategoria_problemu_id')->on('kategoria_problemu')
                ->onDelete('cascade');

            $table->foreign('stav_problemu_id')
                ->references('stav_problemu_id')->on('stav_problemu')
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
        Schema::dropIfExists('problem');
    }
}
