<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateStavRieseniaProblemuTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('stav_riesenia_problemu', function (Blueprint $table) {
            $table->bigIncrements('stav_riesenia_problemu_id');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('stav_riesenia_problemu', function (Blueprint $table) {
            $table->bigInteger('typ_stavu_riesenia_problemu_id')->unsigned();
            $table->bigInteger('problem_id')->unsigned();

            $table->foreign('problem_id')
                ->references('problem_id')->on('problem')
                ->onDelete('cascade');

            $table->foreign('typ_stavu_riesenia_problemu_id')
                ->references('typ_stavu_riesenia_problemu_id')->on('typ_stavu_riesenia_problemu')
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
        Schema::dropIfExists('stav_riesenia_problemu');
    }
}
