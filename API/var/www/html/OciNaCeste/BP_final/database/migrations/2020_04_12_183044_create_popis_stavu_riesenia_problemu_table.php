<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreatePopisStavuRieseniaProblemuTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('popis_stavu_riesenia_problemu', function (Blueprint $table) {
            $table->bigIncrements('popis_stavu_riesenia_problemu_id');
            $table->text('popis');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('popis_stavu_riesenia_problemu', function (Blueprint $table) {
            $table->bigInteger('problem_id')->unsigned();


            $table->foreign('problem_id')
                ->references('problem_id')->on('problem')
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
        Schema::dropIfExists('popis_stavu_riesenia_problemu');
    }
}
